package mylib.terminalredone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mylib.terminalredone.exceptions.MissingParameterException;
import mylib.terminalredone.util.ParameterUtil;
import mylib.util.StringUtil;

/**
 * Terminal function
 * @author Nico
 *
 */
public class TerminalFunction {
	
	/**
	 * call name
	 */
	private final String name;
	
	/**
	 * List of parameters
	 */
	private final List<Parameter> parameters;
	
	/**
	 * the method this funcion points to
	 */
	private final Method method;
	
	/**
	 * text displayed when called help function
	 */
	private final Optional<String> helpText;
	
	String getName() {
		return name;
	}

	List<Parameter> getParameters() {
		return parameters;
	}

	Method getMethod() {
		return method;
	}
	
	String getHelpText() {
		return helpText.orElse(StringUtil.empty());
	}
	
	public TerminalFunction(String name, Method method, String helpText) {
		this.name = name;
		this.method = method;
		this.helpText = Optional.ofNullable(helpText);
		this.parameters = new ArrayList<>();
	}
	
	public void addParameter(Parameter parameter) {
		this.parameters.add(parameter);
	}
	
	public String perform(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MissingParameterException {		
		Object[] orderedParameters = new Object[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			String assignment = ParameterUtil.findAssignment(args, parameters.get(i).getName());
			if (assignment != null) {
				if (parameters.get(i).isArray()) {
					orderedParameters[i] = ParameterUtil.splitValueToArray(assignment);
				} else {
					orderedParameters[i] = ParameterUtil.getValue(assignment);
				}
			} else {
				if (parameters.get(i).isOptional())
					orderedParameters[i] = null; 
				else throw new MissingParameterException(parameters.get(i));
			}
		}
		return (String) method.invoke(null, orderedParameters.length > 0 ? orderedParameters : null);
	}

	public boolean accepts(String[] args) {
		return args[0].equals(name) && args.length-1 <= parameters.size();
	}
	

}
