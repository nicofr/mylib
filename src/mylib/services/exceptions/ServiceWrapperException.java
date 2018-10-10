package mylib.services.exceptions;

public class ServiceWrapperException extends ServiceException {


	private static final long serialVersionUID = 1L;
	
	private final Exception wrappedException;
	
	public ServiceWrapperException(Exception wrappedException) {
		super(ServiceErrorId.ReflectionException);
		this.wrappedException = wrappedException;
	}
	
	public Exception getWrappedException() {
		return wrappedException;
	}

}
