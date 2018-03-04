package mylib.terminalredone.exceptions;

import java.lang.reflect.Method;

public class IllegalTerminalFunctionException extends Exception {
	
	private static final long serialVersionUID = 1715501607458704766L;

	public IllegalTerminalFunctionException(Method m) {
		this.method = m;
	}

	private Method method;
	
	public String toString() {
		return "Method not suitable as terminal functions: " + method + ". Terminal functions have to have "
				+ "return type String and only Accept Parameters of type String or arrays of String and have to be static";
	}

}
