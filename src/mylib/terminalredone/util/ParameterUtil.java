package mylib.terminalredone.util;

import java.util.Arrays;

public class ParameterUtil {
	
	public static String getParameterName(String arg) {
		return arg.split("=")[0];
	}
	
	public static String getValue(String arg) {
		return arg.split("=")[1];
	}
	
	public static String[] splitValueToArray(String arg) {
		return arg.split("=")[1].split(",");
	}
	
	public static boolean isRhsArray(String arg) {
		return arg.split("=")[1].split(",").length > 1;
	}
	
	public static String findAssignment(String[] args, String name) {
		return Arrays.asList(args).stream().filter(s -> {
			return getParameterName(s).equals(name);
		}).findFirst().orElse(null);
	}

}
