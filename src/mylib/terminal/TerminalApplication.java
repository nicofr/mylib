package mylib.terminal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import mylib.lambdautils.ThrowingConsumer;
import mylib.terminal.exceptions.IllegalInteractiveTerminalFunctionException;
import mylib.terminal.exceptions.IllegalPerformException;
import mylib.terminal.exceptions.IllegalTerminalFunctionException;
import mylib.terminal.interactive.InteractiveContext;
import mylib.terminal.interactive.InteractiveTerminalAction;
import mylib.terminal.interactive.InteractiveTerminalFunction; 

/**
 * Basic Application class to be used in other projects.
 * Reads Specified terminal functions and makes them available in terminal
 * @author Nico
 *
 */
public class TerminalApplication {
	
	/**
	 * reserved words that can not be used for custom functions
	 */
	private static final String[] RESERVED_WORDS = {"interactive", "quit"};
	
	/**
	 * registered functions
	 */
	private HashMap<String, TerminalFunction> functions;
	
	/**
	 * registered interactive functions
	 */
	private HashMap<String, InteractiveTerminalFunction> interactiveFunctions;
	
	/**
	 * Singleton instance
	 */
	private static Optional<TerminalApplication> instance = Optional.ofNullable(null);
	
	/**
	 * indicates if interactive mode is allowed
	 */
	private boolean allowInteractiveMode = false;
	
	/**
	 * indicates if application operates currently in interactive mode
	 */
	boolean inInteractiveMode = false;
	
	/**
	 * interactive context class
	 */
	private Class<? extends InteractiveContext> interactiveContextClass;
	
	/**
	 * Tries to invoke the function with name in first argument if functions exists
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IllegalPerformException 
	 */
	public String perform(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalPerformException {	
		if (functions == null) {
			throw new IllegalPerformException("No functions registered");
		}
		if (args.length > 0 && functions.containsKey(args[0])) {
			TerminalFunction f = functions.get(args[0]);
			if (f.accepts(args)) {
				return f.perform(args);
			} else {
				return f.printHelp();
			}
		} else {
			return printUnkownCommandHelp();
		}
	}
	
	/**
	 * all names of registered Terminal functions for this application
	 * @return
	 */
	public Set<String> getRegisteredFunctionNames() {
		Set<String> result = new HashSet<>();
		result.addAll(functions.keySet());
		result.addAll(interactiveFunctions.keySet());
		return result;
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 * @throws IllegalPerformException 
	 */
	private String performInteractive(String[] args, InteractiveContext context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalPerformException {
		if (args.length > 0 && interactiveFunctions.containsKey(args[0])) {
			InteractiveTerminalFunction f = interactiveFunctions.get(args[0]);
			if (f.accepts(args)) {
				return f.perform(args, context);
			} else {
				return f.printHelp();
			}
		} else {
			// try to run non interactively
			return perform(args);
		}
	}
	
	
	/**
	 * check if given word is already reserved by default
	 */
	private static boolean isWordReserved(String word) {
		return Arrays.stream(RESERVED_WORDS).anyMatch(s -> s.equals(word));
	}
	
	/**
	 * make this application allow operate in interactive mode
	 * @param interactiveContextClass
	 */
	public void allowInteractiveMode(Class<? extends InteractiveContext> interactiveContextClass) {
		allowInteractiveMode = true;
		this.interactiveContextClass = interactiveContextClass;
	}
	
	/**
	 * Access to Singleton instance
	 * @return
	 */
	public static TerminalApplication the() {
		if (!instance.isPresent()) {
			instance = Optional.of(new TerminalApplication());
		} 
		return instance.get();
	}
	
	/**
	 * Prints help with notification that command was unknown
	 * @return
	 */
	private static String printUnkownCommandHelp() {
		return "Unkown Command"+System.lineSeparator() + System.lineSeparator()+printHelpFunction();
	}

	/**
	 * Singleton class
	 */
	private TerminalApplication() {
	}

	/**
	 * Checks all functions with TerminalAction annotation in specified classes and adds them if suitable. Throws Exception if 
	 * function violates Terminal function specifications
	 * @param actionDefiningClasses
	 * @throws IllegalTerminalFunctionException
	 * @throws IllegalInteractiveTerminalFunctionException 
	 */
	public void registerTerminalFunctions(Class<?>... actionDefiningClasses) throws IllegalTerminalFunctionException, IllegalInteractiveTerminalFunctionException {
		functions = new HashMap<>();
		// Scan in classes for terminal functions
		for (Class<?> c : actionDefiningClasses) {
			Stream.of(c.getMethods()).forEach((ThrowingConsumer<Method>)f -> {
				if (f.isAnnotationPresent(TerminalAction.class)) {
					if (!isWordReserved(f.getName()))
						emitTerminalfunction(f);
					else throw new IllegalTerminalFunctionException(f);
				}
			});
		}
		
		if (allowInteractiveMode) {
			interactiveFunctions = new HashMap<>();
			for (Class<?> c : actionDefiningClasses) {
				Stream.of(c.getMethods()).forEach((ThrowingConsumer<Method>)f -> {
					if (f.isAnnotationPresent(InteractiveTerminalAction.class)) {
						if (!isWordReserved(f.getName()))
							emitInteractiveTerminalfunction(f);
						else throw new IllegalInteractiveTerminalFunctionException(f);
					}
				});
			}
		}
		
		try {
			// default help function if none yet present
			if (!functions.containsKey("help"))
				emitTerminalfunction(this.getClass().getMethod("printHelpFunction"));
			if (allowInteractiveMode) {
				emitTerminalfunction(StandartFeatures.class.getMethod("startInteractiveFunction"));
				emitTerminalfunction(StandartFeatures.class.getMethod("quitInteractiveFunction")); 
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// should not happen and therefore print stack trace
			e.printStackTrace();
		} 	
	}

	/**
	 * See description for emitTerminalFunction
	 * @param f
	 * @throws IllegalTerminalFunctionException
	 * @throws IllegalInteractiveTerminalFunctionException 
	 */
	private void emitInteractiveTerminalfunction(Method f) throws IllegalTerminalFunctionException, IllegalInteractiveTerminalFunctionException {
		// Only allow static functions that return a String and only have parameters of type String
		if (!(f.getReturnType() == String.class 
				&& Stream.of(Arrays.copyOfRange(f.getParameterTypes(), 1, f.getParameterCount())).allMatch(
						type -> {return type == String.class;})
				&& f.getParameterTypes()[0].getSuperclass() == InteractiveContext.class 
				&& Modifier.isStatic(f.getModifiers()))) {
			throw new IllegalInteractiveTerminalFunctionException(f);
		}
		
		InteractiveTerminalFunction function = new InteractiveTerminalFunction(f.getAnnotation(InteractiveTerminalAction.class).value(), f.getParameterCount()-1);
		AtomicInteger i = new AtomicInteger(0);
		
		// Set parameter help names
		Stream.of(f.getParameters()).forEachOrdered( p -> {
			if (p.getType() == String.class)
				function.setParameterHelpName(p.isAnnotationPresent(ParameterHelpName.class) ? 
					p.getAnnotation(ParameterHelpName.class).value() : p.getName() , i.getAndIncrement());
		});
		
		function.setMethod(f);
		
		// only unique functions
		if (! interactiveFunctions.containsKey(function.getName()))
			interactiveFunctions.put(function.getName(), function);
		
	}

	/**
	 * adds the specified Terminal function
	 * @param f
	 * @throws IllegalTerminalFunctionException
	 */
	private void emitTerminalfunction(Method f) throws IllegalTerminalFunctionException {		
		// Only allow static functions that return a String and only have parameters of type String
		if (!(f.getReturnType() == String.class 
				&& Stream.of(f.getParameterTypes()).allMatch(type -> {return type == String.class;})
				&& Modifier.isStatic(f.getModifiers()))) {
			throw new IllegalTerminalFunctionException(f);
		}
		
		TerminalFunction function = new TerminalFunction(f.getAnnotation(TerminalAction.class).value(), f.getParameterCount());
		AtomicInteger i = new AtomicInteger(0);
		
		// Set parameter help names
		Stream.of(f.getParameters()).forEachOrdered( p -> {
			function.setParameterHelpName(p.isAnnotationPresent(ParameterHelpName.class) ? 
					p.getAnnotation(ParameterHelpName.class).value() : p.getName() , i.getAndIncrement());
		});
		
		function.setMethod(f);
		
		// only unique functions
		if (! functions.containsKey(function.getName()))
			functions.put(function.getName(), function);	
	}
	
	/**
	 * interactive mode
	 */
	void startInteractiveMode() {
		InteractiveContext interactiveContext;
		try {
			interactiveContext = interactiveContextClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		
		inInteractiveMode = true;
		
		while (inInteractiveMode) {
			try {
				System.out.print("\\>");
				System.out.println(performInteractive(System.console().readLine().split("\\s"), interactiveContext));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				break;
			} catch (IllegalPerformException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
//======================PREDEFINED FUNCTIONS========================================================	
	
	@TerminalAction(value = "help")
	public static String printHelpFunction() {
		StringBuilder stringBuilder = new StringBuilder();
		TerminalApplication.the().functions.forEach((key,f) -> {
			stringBuilder.append(f.printHelp());
			stringBuilder.append(System.lineSeparator());
		});
		if (TerminalApplication.the().inInteractiveMode)
			TerminalApplication.the().interactiveFunctions.forEach((key, f) -> {
				stringBuilder.append(f.printHelp() +" -- interactive");
				stringBuilder.append(System.lineSeparator());
			}); 
		return stringBuilder.toString();
	}	
	
}
