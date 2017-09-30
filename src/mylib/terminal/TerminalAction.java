package mylib.terminal;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TerminalAction {

	public String value();
	
}
