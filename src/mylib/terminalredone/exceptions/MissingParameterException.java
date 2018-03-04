package mylib.terminalredone.exceptions;

import mylib.terminalredone.Parameter;

@SuppressWarnings("serial")
public class MissingParameterException extends IllegalPerformException {

	public MissingParameterException(Parameter parameter) {
		super("Parameter: "+parameter.getName() + " is not Optional. Please provide value.");
	}

}
