package ch.awae.simtrack.util;

import java.util.function.Function;

import lombok.AllArgsConstructor;

public interface DataMapper<T> {

	public T get();

	public void set(T value);
	
	default void update(Function<T, ? extends T> f) {
		set(f.apply(get()));
	}

	@AllArgsConstructor
	public static class Store<T> implements DataMapper<T> {

		private T value;

		@Override
		public T get() {
			return this.value;
		}

		@Override
		public void set(T value) {
			this.value = value;
		}
	}
}
