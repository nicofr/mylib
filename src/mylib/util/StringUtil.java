package mylib.util;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Splitter;

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
	
	public static List<String> splitToListAtLineEndings(String arg) {
		return Arrays.asList(arg.split("\\r?\\n"));
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
	
	/**
	 * 
	 * @param arg
	 * @return
	 */
	public static String joinWithSeparator(List<String> arg, String separator) {
		StringBuilder sb = new StringBuilder();
		arg.stream().forEachOrdered(s -> sb.append(s + separator));
		return sb.toString().substring(0, sb.toString().length()-separator.length());
	}
	
	/**
	 * splits string into chunks of equals size (apart from last one) separated by line separator
	 * @param arg
	 * @param limit
	 * @return
	 */
	public static String splitIntoLimitedLines(String arg, int limit) {
		return Splitter.fixedLength(limit).splitToList(arg).stream().reduce((s1, s2) -> {
			return s1 + System.lineSeparator() + s2;
		}).orElse(StringUtil.empty());
	}
	
	/**
	 * splits string into list of strings with given length
	 * @param arg
	 * @param length
	 * @return
	 */
	public static List<String> splitToListFixesLength(String arg, int length) {
		return Splitter.fixedLength(length).splitToList(arg);
	}

}
