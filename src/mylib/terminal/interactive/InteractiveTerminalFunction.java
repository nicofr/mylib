package mylib.terminal.interactive;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import mylib.terminal.TerminalFunction;

public class InteractiveTerminalFunction extends TerminalFunction {

	public InteractiveTerminalFunction(String name, int parameters) {
		super(name, parameters);
	}
	
	@Override
	public String perform(String[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		throw new IllegalAccessException();
	}
	
	public String perform(String[] args, InteractiveContext interactiveContext) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (String) getMethod().invoke(null, args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : null, interactiveContext);
	}

}
