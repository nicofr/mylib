package mylib.sessions.exceptions;

public class NoSessionWithSessionIdException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int id;
	
	public NoSessionWithSessionIdException(int theid) {
		this.id = theid;
	}
	
	public int getId() {
		return id;
	}

}
