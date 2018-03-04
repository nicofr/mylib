package mylib.terminalredone.test;

import java.lang.reflect.InvocationTargetException;

import mylib.terminalredone.TerminalApplication;
import mylib.terminalredone.exceptions.IllegalPerformException;
import mylib.terminalredone.exceptions.IllegalTerminalFunctionException;

public class Terminal {
	
	public static void main(String[] args) throws IllegalTerminalFunctionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IllegalPerformException {
		TerminalApplication.the().registerTerminalFunctions(Features.class);
		System.out.println(TerminalApplication.the().perform(args));
	}
	
	

}
