package mylib.terminal;

public class Example {
	
	@TerminalAction(value = "example")
	public static String ExampleTerminalFunction(@ParameterHelpName(value = "name of param1") String param1, String param2) {
		return param1 + " " + param2;
	}	
}
