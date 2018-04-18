package mylib.terminal.exceptions;

import mylib.terminal.Parameter;

@SuppressWarnings("serial")
public class MissingParameterException extends IllegalPerformException {

	public MissingParameterException(Parameter parameter) {
		super("Parameter: "+parameter.getName() + " is not Optional. Please provide value.");
	}

}
