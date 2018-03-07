package mylib.terminalredone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import mylib.lambdautils.ThrowingConsumer;
import mylib.terminal.exceptions.IllegalInteractiveTerminalFunctionException;
import mylib.terminalredone.annotations.Name;
import mylib.terminalredone.annotations.Optional;
import mylib.terminalredone.annotations.TerminalAction;
import mylib.terminalredone.annotations.TerminalHelp;
import mylib.terminalredone.exceptions.IllegalPerformException;
import mylib.terminalredone.exceptions.IllegalTerminalFunctionException;

public class TerminalApplication {
	
	/**
	 * reserved words that can not be used for custom functions
	 */
	private static final String[] RESERVED_WORDS = {"help"};
	
	/**
	 * all funcions with identifiers
	 */
	private final Map<String, TerminalFunction> functions;
	
	/**
	 * singleton instance
	 */
	private static java.util.Optional<TerminalApplication> instance = java.util.Optional.ofNullable(null);
	
	private TerminalApplication() {
		functions = new HashMap<>();
	}
	
	public static TerminalApplication the() {
		if (! instance.isPresent()) {
			instance = java.util.Optional.of(new TerminalApplication());
		} 
		return instance.get();
	}
	
	/**
	 * check if given word is already reserved by default
	 */
	private static boolean isWordReserved(String word) {
		return Arrays.stream(RESERVED_WORDS).anyMatch(s -> s.equals(word));
	}
	
	
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
				return printHelp(f);
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
		return printHelpFunction();
	}
	
	/**
	 * Checks all functions with TerminalAction annotation in specified classes and adds them if suitable. Throws Exception if 
	 * function violates Terminal function specifications
	 * @param actionDefiningClasses
	 * @throws IllegalTerminalFunctionException
	 * @throws IllegalInteractiveTerminalFunctionException 
	 */
	public void registerTerminalFunctions(Class<?>... actionDefiningClasses) throws IllegalTerminalFunctionException{
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
			emitTerminalfunction(this.getClass().getMethod("printHelpFunction"));
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
						&& Stream.of(f.getParameterTypes()).allMatch(type -> {return type == String.class || type == String[].class;})
						&& Modifier.isStatic(f.getModifiers()))) {
					throw new IllegalTerminalFunctionException(f);
				}
				
				String name = f.isAnnotationPresent(TerminalAction.class) ? f.getAnnotation(TerminalAction.class).value() : f.getName();
				String helpText = f.isAnnotationPresent(TerminalHelp.class) ? f.getAnnotation(TerminalHelp.class).value() : null;
				
				TerminalFunction function = new TerminalFunction(name, f, helpText);
				for (int i = 0; i < f.getParameterCount(); i++) {
					Parameter paramater = new Parameter(f.getParameters()[i].isAnnotationPresent(Name.class) ? f.getParameters()[i].getAnnotation(Name.class).value() : f.getParameters()[i].getName(),
							f.getParameters()[i].isAnnotationPresent(Optional.class),
							f.getParameters()[i].isAnnotationPresent(TerminalHelp.class) ? f.getParameters()[i].getAnnotation(TerminalHelp.class).value() : "",
							f.getParameterTypes()[i] == String[].class);
					function.addParameter(paramater);
				}
				
				// only unique functions
				if (! functions.containsKey(function.getName()))
					functions.put(function.getName(), function);	
	}	
	
	/**
	 * prints help for one specific function
	 * @param f
	 * @return
	 */
	static String printHelp(TerminalFunction f) {
		StringBuilder sb = new StringBuilder();
		sb.append(">"+f.getName() + "\t\t" + f.getHelpText() + System.lineSeparator());
		for (Parameter p : f.getParameters()) {
			sb.append(p.getName());
			sb.append("\t\t" + p.getHelpText() + (p.isOptional() ? "(Optional)" : "") + (p.isArray() ? "(Array)" : "") +System.lineSeparator());
		}
		return sb.toString();
	}
	
	@TerminalAction("help")
	public static String printHelpFunction() {
		StringBuilder sb = new StringBuilder();
		for (TerminalFunction f : the().functions.values()) {
			sb.append(printHelp(f)+System.lineSeparator());
		}
		return sb.toString();
	}

}
