package ch.awae.utils.functional;

import java.util.function.Function;

public interface Try<T> {

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    T get() throws Throwable;

    Throwable getFailure();

    <S> Try<S> map(FailableFunction1<T, S> f);

    <S> Try<S> flatMap(Function<T, Try<S>> f);

    Try<T> recover(FailableFunction1<Throwable, Try<T>> f);

    // FACTORY METHODS

    public static <T> Try<T> success(T value) {
        return new Success<T>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Try<T> failure(Throwable throwable) {
        return new Failure(throwable);
    }

    public static <T> Try<T> of(FailableFunction0<T> f) {
        try {
            return success(f.apply());
        } catch (Throwable t) {
            return failure(t);
        }
    }

}
