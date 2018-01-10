package mylib.terminal.exceptions;

import java.lang.reflect.Method;

public class IllegalInteractiveTerminalFunctionException extends Exception {
	
	public IllegalInteractiveTerminalFunctionException(Method m) {
		this.method = m;
	}

	private Method method;
	
	public String toString() {
		return "Method not suitable as terminal functions: " + method + ". Terminal functions have to have "
				+ "return type String and only Accept Parameters of type String and be static. "
				+ "Additionally interactive functions have to have one parameter of type InteractiveContext at last Parameter";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 627238480072056404L;

}
