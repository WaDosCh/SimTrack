package ch.awae.simtrack.util;

import lombok.AllArgsConstructor;

public interface DataMapper<T> {

	public T get();

	public void set(T value);

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
