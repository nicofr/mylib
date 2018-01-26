package mylib.util;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Encapsulates an element that can only by written once and is then final.
 * The difference to final is that it can be written anywhere in the code and not only in Constructors.
 * @author Nico
 *
 */
public class OnceWriteable<TYPE> {
	
	/**
	 * the encapsulated object
	 */
	private Optional<TYPE> arg;
	
	/**
	 * Does not write encapsulated object
	 */
	public OnceWriteable() {
		this.arg = Optional.ofNullable(null);
	}
	
	/**
	 * Writes encapsulated object
	 * @param arg
	 */
	public OnceWriteable(TYPE arg) {
		this.arg = Optional.of(arg);
	}
	
	/**
	 * checks if encapsulated object is already set
	 * @return
	 */
	public boolean isSet() {
		return arg.isPresent();
	}
	
	/**
	 * sets encapsulated object if possible. If not, throws Exception
	 * @param arg
	 * @throws AlreadySetException
	 */
	public void set(TYPE arg) throws AlreadySetException {
		if (! this.arg.isPresent()) {
			this.arg = Optional.of(arg);
		} else {
			throw new AlreadySetException();
		}		
	}
	
	/**
	 * checks if object can be written
	 * @return
	 */
	public boolean canSet() {
		return !isSet();
	}
	
	/**
	 * getter for encapsulated object if already set. Returns null if not set
	 * @return
	 */
	public TYPE get() {
		return arg.orElse(null);
	}
	
	/**
	 * sets if possible. Else invokes the else action
	 * @param arg
	 * @param elseAction
	 */
	public void setOrElse(TYPE arg, Consumer<TYPE> elseAction) {
		if (canSet()) {
			this.arg = Optional.of(arg);
		} else {
			elseAction.accept(arg);
		}
	}
	
	/**
	 * Sets value if possible. If not, does noting
	 * @param arg
	 */
	public void setOrElseNothing(TYPE arg) {
		if (canSet()) {
			this.arg = Optional.of(arg);
		}
	}
	
	/**
	 * gets encapsulated object if present. Else the other object
	 * @return
	 */
	public TYPE getOrElse(TYPE other) {
		return arg.orElse(other);
	}
	

}
