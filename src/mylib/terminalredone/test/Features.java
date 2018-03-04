package mylib.terminalredone.test;

import java.util.Arrays;

import mylib.terminalredone.annotations.Name;
import mylib.terminalredone.annotations.Optional;
import mylib.terminalredone.annotations.TerminalAction;
import mylib.terminalredone.annotations.TerminalHelp;
import mylib.util.StringUtil;

public class Features {
	
	@TerminalAction("testnoparam")
	public static String testnoparam() {
		return "done";
	}
	
	@TerminalAction("testbase")
	@TerminalHelp("this is the help text for the base function")
	public static String testbase(@TerminalHelp("this value is only echoed back") @Name("name") String name) {
		return name;
	}
	
	@TerminalAction("testoptional")
	public static String testoptional(@TerminalHelp("this is not optional") @Name("notoptional") String notoptional, @TerminalHelp("this is optional")@Optional @Name("optional") String optional) {
		if (optional != null) {
			return notoptional + " " + optional;
		} else {
			return notoptional;
		}
	}

	@TerminalAction("testarray")
	public static String testarray(@TerminalHelp("this must be an array")String[] arg0) {
		return StringUtil.join(Arrays.asList(arg0));
	}
	
	@TerminalAction("testmoreparams")
	public static String testmoreparams(String arg0,
			@Optional String arg1,
			String[] arg2,
			@TerminalHelp("help text for arg3") String arg3,
			@Optional @TerminalHelp("help text for arg 4 which is an array") String[] arg4) {
		return arg0 + (arg1==null ? "" : arg1) + StringUtil.join(Arrays.asList(arg2)) + arg3 + (arg4 == null ? "" : StringUtil.join(Arrays.asList(arg4)));
	}
}
