package mylib.terminal.interactive;

import java.lang.reflect.InvocationTargetException;

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
		if (args.length > 1) 
		{
			Object[] param = new Object[args.length];
			param[0] = interactiveContext;
			for (int i = 1; i < args.length; i++) param[i] = args[i];
			return (String) getMethod().invoke(null, param); 
		}
		else
			return (String) getMethod().invoke(null, interactiveContext);
	}

}
