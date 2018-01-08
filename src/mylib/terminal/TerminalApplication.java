package mylib.terminal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import mylib.lambdautils.ThrowingConsumer;
import mylib.terminal.exceptions.IllegalTerminalFunctionException;
import mylib.util.StringUtil;

/**
 * Basic Application class to be used in other projects.
 * Reads Specified terminal functions and makes them available in terminal
 * @author Nico
 *
 */
public class TerminalApplication {
	
	private static HashMap<String, TerminalFunction> functions;
	
	private static final String[] reserverdWords  = {"exit", "interactive"};
	
	public static boolean isInteractive = false;
	public static boolean exit = false;
	public static boolean allowInteractiveMode = false;
	
	/**
	 * Tries to invoke the function with name in first argument if functions exists
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static String perform(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {	
		if (functions.containsKey(args[0])) {
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
	public static void registerTerminalFunctions(Class<?>... actionDefiningClasses) throws IllegalTerminalFunctionException {
		functions = new HashMap<>();
		// Scan in classes for terminal functions
		for (Class<?> c : actionDefiningClasses) {
			Stream.of(c.getMethods()).forEach((ThrowingConsumer<Method>)f -> {
				if (f.isAnnotationPresent(TerminalAction.class) && !isReserved(f.getName())) {
					emitTerminalfunction(f);
				}
			});
		}
		
		try {
			// default help function if none yet present
			if (!functions.containsKey("help"))
				emitTerminalfunction(TerminalApplication.class.getMethod("printHelpFunction"));
			if (allowInteractiveMode) {
				emitTerminalfunction(TerminalApplication.class.getMethod("enableInteractiveModeFunction"));
				emitTerminalfunction(TerminalApplication.class.getMethod("exitFunction"));
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// should not happen and therefore print stack trace
			e.printStackTrace();
		} 	
	}

	private static boolean isReserved(String name) {
		return Arrays.stream(reserverdWords).anyMatch(s -> s.equals(name));
	}

	/**
	 * adds the specified Terminal function
	 * @param f
	 * @throws IllegalTerminalFunctionException
	 */
	private static void emitTerminalfunction(Method f) throws IllegalTerminalFunctionException {		
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
	
	
	@TerminalAction(value = "help")
	public static String printHelpFunction() {
		StringBuilder stringBuilder = new StringBuilder();
		functions.forEach((key, f) -> {
			stringBuilder.append(f.printHelp());
			stringBuilder.append(System.lineSeparator());
		});
		return stringBuilder.toString();
	}
	
	@TerminalAction("interactive")
	public static String enableInteractiveModeFunction() {
		isInteractive = true;
		return StringUtil.empty();
	}
	
	@TerminalAction("exit")
	public static String exitFunction() {
		isInteractive = false;
		exit = true;
		return StringUtil.empty();
	}
	
}
