package mylib.services.exceptions;

public enum ServiceErrorId {
	
	
	
	ServiceNameInUse("Service name is already in use"),
	ReflectionException("Internal error"),
	InvalidService("Service is invalid"),
	FieldIsNoParameter("Field is not a parameter"),
	ParamNameInUse("Parameter name is already in use"),
	InvalidParameter("Field is not a valid parameter"),
	NoSuchParameter("No such parameter"),
	NoSuchService("No such service"), FieldIsNoFlag("Field is not a flag"), InvalidFlag("Field is not a valid flag"), NoSuchFlag("No such flag"), ParamNotOptional("Paramer is not optional");
	
	private final String message;
	
	private ServiceErrorId(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
