package mylib.terminal.test;

import mylib.terminal.TerminalAction;
import mylib.terminal.interactive.InteractiveTerminalAction;

public class FeaturesTest {
	
	@TerminalAction(value = "example")
	public static String ExampleTerminalFunction(String param1) {
		return param1;
	}
	
	@InteractiveTerminalAction("set")
	public static String ExampleInteractiveTerminalFunction(InteractiveTestContext context, String param1) {
		context.count = Integer.parseInt(param1);
		return String.valueOf(context.count);
	}
	

}
