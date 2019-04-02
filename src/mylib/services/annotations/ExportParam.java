package mylib.services.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import mylib.services.components.standardmappers.IdentityMapper;

@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportParam {
	
	String Ident();
	Class<?> MapperClass() default IdentityMapper.class;
	boolean Optional() default false;
	String Help() default "";
	
}
