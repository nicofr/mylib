package mylib.terminal.test;

import java.lang.reflect.InvocationTargetException;

import mylib.terminal.TerminalApplication;
import mylib.terminal.exceptions.IllegalInteractiveTerminalFunctionException;
import mylib.terminal.exceptions.IllegalPerformException;
import mylib.terminal.exceptions.IllegalTerminalFunctionException;

public class TestTerminal {
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalPerformException, IllegalTerminalFunctionException, IllegalInteractiveTerminalFunctionException, NoSuchMethodException, SecurityException {
		TerminalApplication.the().allowInteractiveMode(InteractiveTestContext.class);
		TerminalApplication.the().registerTerminalFunctions(FeaturesTest.class);
	
		System.out.println(TerminalApplication.the().perform(args));
	}

}
