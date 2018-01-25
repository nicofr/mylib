package mylib.lambdautils;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, R> extends BiConsumer<T, R> {

    @Override
    default void accept(final T elem, final R elem2) {
        try {
            acceptThrows(elem, elem2);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T elem, R elem2) throws Exception;

}