package mylib.services.exceptions;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -960475780948004202L;
	
	private final ServiceErrorId errorId;
	private final String info;
	
	public ServiceException(ServiceErrorId errorId) {
		this.errorId = errorId;
		this.info = "";
	}
	
	public ServiceException(ServiceErrorId errorId, String info) {
		this.errorId = errorId;
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}
	
	public ServiceErrorId getErrorId() {
		return this.errorId;
	}

	

}
