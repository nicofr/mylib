package mylib.simpleterminal;

import java.util.List;

public class TerminalUtils {

	public static String getFunctionName(String[] args) {
		if (args.length > 0) {
			return args[0];
		} else return null;
	}
	
	public static boolean hasParamArguments(String[] args, String name) {
		int pos = getParamPos(args, name);
		if (pos < 0) {
			return ! isParamName(args[pos+1]);
		} 
		return false;
	}
	
	private static boolean isParamName(String string) {
		return string != null && string.length() > 0 ? string.charAt(0) == '-' : false;
	}

	public static String getParamArgument(String[] args, String name) {
		int pos = getParamPos(args, name);
		if (pos > 0 && args.length > pos + 1) {
			return args[pos+1];
		} else return null;
	}
	
	public static boolean hasParam(String[] args, String name) {
		return getParamPos(args, name) > 0;
	}
	
	public static List<String> getParamArgumentsList(String[] args, String name) {
		return null;
	}

	private static int getParamPos(String[] args, String name) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(name)) return i;
		}
		return -1;
	}
	
}
