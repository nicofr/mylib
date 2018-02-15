package mylib.lambdautils;

import java.util.function.*;

public interface ThrowingPredicate<T> extends Predicate<T> {

	@Override
	default boolean test(T arg0) {
		 try {
	            return testThrows(arg0);
	        } catch (final Exception e) {
	            throw new RuntimeException(e);
	        }
	}

	
	boolean testThrows(T elem) throws Exception;
}
