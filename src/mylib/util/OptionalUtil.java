package mylib.util;

import java.util.Optional;

public class OptionalUtil {
	
	public static <TYPE> Optional<TYPE> empty() {
		return Optional.ofNullable(null);
	}

}
