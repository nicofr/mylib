package mylib.terminal;

/**
 * Parameter of terminal function. Can be Optional. Can be set via "parametername=value"
 * @author Nico
 *
 */
public class Parameter {
	
	/**
	 * The set name
	 */
	private final String name;
	
	/**
	 * denotes if this parameter is optional
	 */
	private final boolean optional;
	
	/**
	 * printed when help function is called
	 */
	private final String helpText; 
	
	/**
	 * denotes if this parameter is an array
	 */
	private final boolean isArray;
	
	/**
	 * Constructor
	 * @param name
	 * @param optional
	 * @param helpText
	 */
	public Parameter(String name, boolean optional, String helpText, boolean isArray) {
		this.name = name;
		this.isArray = isArray;
		this.optional = optional;
		this.helpText = helpText;
	}

	public boolean isArray() {
		return isArray;
	}

	public String getHelpText() {
		return helpText;
	}

	public boolean isOptional() {
		return optional;
	}

	public String getName() {
		return name;
	}
	
	

}
