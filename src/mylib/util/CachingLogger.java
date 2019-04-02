package mylib.util;

import java.util.ArrayList;
import java.util.List;

public class CachingLogger {
	
	// CONSTANTS
	
	private final static int CACHE_SIZE = 1000;
	private static final String DEFAULT_LOG_FILE_PATH = System.getProperty("user.dir")+"\\LOG_"+System.currentTimeMillis()+".txt";
	private final static CachingLogger instance = new CachingLogger();
	private final static int DEFAULT_LEVEL_OFFSET_SIZE = 3;
	private static final String LOGMESSAGE = "LOG ";
	private static final String WARNMESSAGE = "WARN ";
	private static final String ERRMESSAGE = "ERR ";
	
	// ATTRIBUTES
	
	private String logFilePath;
	
	private int levelOffsetSize;
	
	private String currentLevelOffset;
	
	private final List<String> cache;
	
	private CachingLogger() {
		this.logFilePath = DEFAULT_LOG_FILE_PATH;
		levelOffsetSize = DEFAULT_LEVEL_OFFSET_SIZE;
		currentLevelOffset = "";
		cache = new ArrayList<>();
		
	}
	
	// INTERNAL FUNCTIONS 
	
	private void _push() {
		currentLevelOffset = currentLevelOffset + " ";
	}
	
	private void _pop() {
		if (!currentLevelOffset.equals(""))
			currentLevelOffset = currentLevelOffset.substring(0, currentLevelOffset.length()-1);
	}
	
	private void write(String prefix, String message) {
		cache.add(prefix + currentLevelOffset + message);
		if (cache.size() >= CACHE_SIZE) {
			persist();
		}
	}
	
	private void persist() {
		
	}
	
	private void _log(String message) {
		write(LOGMESSAGE, message);
	}
	
	private void _warn(String message) {
		write (WARNMESSAGE, message);
	}
	
	private void _err(String message) {
		write(ERRMESSAGE, message);
	}
	
	// PUBLIC FUNCTIONS
	
	public static void log(String message) {
		instance._log(message);
	}
	
	public static void warn(String message) {
		instance._warn(message);
	}
	
	public static void err(String message) {
		instance._err(message);
	}
	
	public static void push() {
		instance._push();
	}
	
	public static void pop() {
		instance._pop();
	}
	

}
