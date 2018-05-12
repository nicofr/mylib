package mylib;

/**
 * Most abstract logic bean. Convention: after constructor, logic is ready to be performed. Calling perfom twice must result in same results
 * @author Nico
 *
 * @param <RESTYPE>
 * @param <EXTYPE>
 */
public interface ApplicationLogicController <RESTYPE extends ControllerResult, EXTYPE extends Exception> {
	
	public RESTYPE perform() throws EXTYPE;

}
