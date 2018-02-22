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
	 * @param arg
	 * @return
	 */
	public static String joinWithLineSeperator(List<String> arg) {
		StringBuilder sb = new StringBuilder();
		arg.stream().forEachOrdered(s -> sb.append(s + System.lineSeparator()));
		return sb.toString();
	}

}
