package mylib.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Logger
 * @author Nico
 *
 */
public class Logger {
	
	/**
	 * singleton instance
	 */
	private static final Logger the;
	
	private boolean dologging = false;
	
	private static final int CACHE_SIZE = 1000;
	private static final String LOG_MSG = "LOG  ";
	private static final String WARN_MSG = "WARN  ";
	private static final String ERR_MSG = "ERR  ";
	private static final int MAX_OFFSET_SIZE = 20;
	private static final int MAX_OFFSET_UNIT_SIZE = 7;
	private static final String MAX_OFFSET_UNIT = "       ";
	
	private static final String LOG_FILE = System.getProperty("user.dir")+"\\LOG_"+System.currentTimeMillis()+".txt";
	
	
	private int offsetUnitSize;
	private String offset;
	
	/**
	 * cache
	 */
	private final List<String> cache;
	
	static {
		the = new Logger();
	}
	
	public static void setLogging(boolean arg) {
		the.dologging = arg;
	}
	
	/**
	 * singleton constructor
	 */
	private Logger() {
		cache = new ArrayList<>();
		offset = "";
		offsetUnitSize = 1;
	}
	
	private String getOffset() {
		return offset;
	}
	
	/**
	 * shifts offset one unit forwards if offset did not exceed max offset size
	 */
	public static void addOffset() {
		the._addOffset();
	}

	private void _addOffset() {
		if (dologging)
		if (offset.length() <= MAX_OFFSET_SIZE * MAX_OFFSET_UNIT_SIZE) {
			offset += MAX_OFFSET_UNIT.substring(0, offsetUnitSize);
		}
	}
	
	/**
	 * sets offset unit size if > 0 and < max size. If not, sets either to 1 or max
	 * @param offsetUnitSize
	 * @return
	 */
	public static void setOffsetUnitSize(int offsetUnitSize) {
		the._setOffsetUnitSize(offsetUnitSize); 
	}
	
	private void _setOffsetUnitSize(int offsetUnitSize2) {
		if (dologging)
		if (offsetUnitSize2 <= 0) {
			offsetUnitSize = 1;
		} else {
			offsetUnitSize = Math.min(offsetUnitSize2, MAX_OFFSET_UNIT_SIZE);
		}
	}

	/**
	 * shifts offset one unit to left if offset did not exceed max unit size
	 */
	public static void removeOffset() {
		the._removeOffset();
	}


	private void _removeOffset() {
		if (dologging)
		if (offset.length() >= offsetUnitSize) {
			offset = offset.substring(0, offset.length()-offsetUnitSize);
		}
	}

	/**
	 * persists current cache to log file
	 */
	public static void persist() {
		the._persist();
	}
	
	private void _persist() {
		if (dologging)
		try {
			FileUtils.writeLines(new File(LOG_FILE), cache);
		} catch (IOException e) {
			//we just accept when logging fails
			System.out.println("logging failed");
		}
		cache.clear();
	}
	
	/**
	 * force cache to persist
	 */
	public static void persistIfCacheFull() {
		the._persistIfCacheFull();
	}

	private void _persistIfCacheFull() {
		if (dologging)
		if (cache.size() >= CACHE_SIZE) {
			persist();
		}
	}
	
	/**
	 * log message 
	 * @param arg
	 */
	public static void log(String arg) {
		the._log(arg);
	}

	private void _log(String arg) {
		if (dologging)
		_writeAndPersist(LOG_MSG + arg);
	}
	
	/**
	 * log warning message
	 * @param arg
	 */
	public static void warn(String arg) {
		the._warn(arg);
	}
	
	private void _warn(String arg) {
		if (dologging)
		_writeAndPersist(WARN_MSG + arg);
	}
	
	/**
	 * logs error message
	 * @param arg
	 */
	public static void err(String arg) {
		the._err(arg);
	}

	private void _err(String arg) {
		if (dologging)
		_writeAndPersist(ERR_MSG + arg);
	}

	private void _writeAndPersist(String arg) {
		if (dologging) {
			cache.add(getOffset() + arg);
			_persistIfCacheFull();
		}
	}
	

}
