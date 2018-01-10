package mylib.terminal.exceptions;

public class IllegalPerformException extends Exception {
	

	private String message;

	public IllegalPerformException(String string) {
		this.message = string;
	}
	
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6798043538471015173L;

}
