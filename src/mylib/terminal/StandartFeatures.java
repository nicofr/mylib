package mylib.terminal;

import mylib.util.StringUtil;

public class StandartFeatures {
	
	@TerminalAction("interactive")
	public static String startInteractiveFunction() {
		TerminalApplication.the().startInteractiveMode();
		return StringUtil.empty();
	}
	
	@TerminalAction("quit")
	public static String quitInteractiveFunction() {
		TerminalApplication.the().inInteractiveMode = false;
		return StringUtil.empty();
	}

}
