package ch.awae.simtrack.core;

public interface BaseComponent<T extends Scene<T>> {

	default String getName() {
		return this.getClass().getSimpleName();
	}
	
}
