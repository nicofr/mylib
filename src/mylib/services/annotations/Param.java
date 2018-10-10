package mylib.services.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface Param {
	
	String Ident();
	String MapperClassName();
	boolean Optional() default false;
	
}
