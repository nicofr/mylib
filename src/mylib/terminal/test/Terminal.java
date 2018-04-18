package mylib.terminal.test;

import java.lang.reflect.InvocationTargetException;

import mylib.terminal.TerminalApplication;
import mylib.terminal.exceptions.IllegalPerformException;
import mylib.terminal.exceptions.IllegalTerminalFunctionException;

public class Terminal {
	
	public static void main(String[] args) throws IllegalTerminalFunctionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalPerformException {
		TerminalApplication.the().registerTerminalFunctions(Features.class);
		System.out.println(TerminalApplication.the().perform(args));
	}
	
	

}
