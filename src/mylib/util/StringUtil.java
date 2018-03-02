package mylib.util;

import java.util.List;

public class StringUtil {
	
	/**
	 * 
	 * @return
	 */
	public static String empty() {
		return new String();
	}
	
	/**
	 * 
	 * @param arg
	 * @return
	 */
	public static String join(List<String> arg) {
		StringBuilder sb = new StringBuilder();
		arg.stream().forEachOrdered(s -> sb.append(s));
		return sb.toString();
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	public static String concatIgnoringNulls(String... args) {
		String res = "";
		for (int i = 0; i < args.length; i++) {
			res = res + (args[i] == null ? "" : args[i]);
		}
		return res;
	}
	
	/**
	 * 
	 * @param startInclusive
	 * @param endExclusive
	 * @param replacement
	 * @return
	 */
	public static String replace(int startInclusive, int endExclusive, String replacement, String arg) {
		if (arg.length() <= startInclusive) return arg;
		return arg.substring(0, startInclusive) + replacement + arg.substring(Math.min(endExclusive, arg.length()));
	}
	
	/**
	 * 
	 * @param arg
	 * @return
	 */
	public static String joinWithLineSeperator(List<String> arg) {
		StringBuilder sb = new StringBuilder();
		arg.stream().forEachOrdered(s -> sb.append(s + System.lineSeparator()));
		return sb.toString();
	}

}
