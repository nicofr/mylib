package mylib.terminal;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ParameterHelpName {

	public String value();
}
