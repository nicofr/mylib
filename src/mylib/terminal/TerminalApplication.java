package mylib.terminal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import mylib.lambdautils.ThrowingConsumer;
import mylib.terminal.exceptions.IllegalTerminalFunctionException;
import mylib.terminal.interactive.InteractiveContext;
import mylib.terminal.interactive.InteractiveTerminalAction;
import mylib.util.StringUtil;

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
	 * Singleton instance
	 */
	private static Optional<TerminalApplication> instance;
	
	/**
	 * indicates if interactive mode is allowed
	 */
	private boolean allowInteractiveMode = false;
	
	/**
	 * indicates if application operates currently in interactive mode
	 */
	private boolean inInteractiveMode = false;
	
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
	 */
	public String perform(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {	
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
	 * 
	 * @param args
	 * @return
	 */
	public String performInteractive(String[] args, InteractiveContext context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return null;
	}
	
	
	/**
	 * check if given word is already reserved by default
	 */
	private static boolean isWordReserved(String word) {
		for (int i = 0; i < RESERVED_WORDS.length; i++) 
			if (word.equals(RESERVED_WORDS[i])) return true;
		return false;
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
	 */
	public void registerTerminalFunctions(Class<?>... actionDefiningClasses) throws IllegalTerminalFunctionException {
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
		
		try {
			// default help function if none yet present
			if (!functions.containsKey("help"))
				emitTerminalfunction(TerminalApplication.class.getMethod("printHelpFunction"));
			if (allowInteractiveMode) 
				emitTerminalfunction(TerminalApplication.class.getMethod("interactive"));
		} catch (NoSuchMethodException | SecurityException e) {
			// should not happen and therefore print stack trace
			e.printStackTrace();
		} 	
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
	private void startInteractiveMode() {
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
				performInteractive(System.console().readLine().split(" "), interactiveContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
//======================PREDEFINED FUNCTIONS========================================================	
	
	@TerminalAction(value = "help")
	public static String printHelpFunction() {
		StringBuilder stringBuilder = new StringBuilder();
		the().functions.forEach((key, f) -> {
			stringBuilder.append(f.printHelp());
			stringBuilder.append(System.lineSeparator());
		});
		return stringBuilder.toString();
	}
	
	@TerminalAction("interactive")
	public static String startInteractiveFunction() {
		the().startInteractiveMode();
		return StringUtil.empty();
	}
	
	@InteractiveTerminalAction("quit")
	public static String quitInteractiveFunction(InteractiveContext context) {
		the().inInteractiveMode = false;
		return StringUtil.empty();
	}
	
}
