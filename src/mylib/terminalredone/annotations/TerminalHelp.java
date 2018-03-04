package mylib.terminalredone.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface TerminalHelp {

	public String value();
	
}
