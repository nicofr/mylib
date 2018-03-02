package mylib.lambdautils;

import java.util.function.Function;

public interface ThrowingFunction<T, R> extends Function<T, R> {
	
    @Override
    default R apply(final T arg) {
        try {
            return applyThrows(arg);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T elem) throws Exception;
}
