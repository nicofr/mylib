package mylib.terminal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class TerminalFunction {
	
	private final String name;
	private final int parameters;
	private String[] parameterHelpNames;
	private Method method;
	
	public TerminalFunction(String name, int parameters) {
		this.name = name;
		this.parameters = parameters;
		parameterHelpNames = new String[parameters];
	}
	
	public final void setParameterHelpName(String name, int pos) {
		parameterHelpNames[pos] = name;
	}
	
	public final void setMethod(Method m) {
		this.method = m;
	}
	
	public String perform(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (String) method.invoke(null, args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null);
	}

	public boolean accepts(String[] args) {
		return args.length -1 == parameters;
	}

	public String printHelp() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(name + " ");
		Stream.of(parameterHelpNames).forEachOrdered(pname -> {
			stringBuilder.append("[" + pname + "] ");
		});
		return stringBuilder.toString();
	}

	public final String getName() {
		return name;
	}
	
	protected Method getMethod() {
		return method;
	}

}
