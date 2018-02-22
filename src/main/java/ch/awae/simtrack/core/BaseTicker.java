package ch.awae.simtrack.core;

public interface BaseTicker<T extends Scene<T>> {

	void tick(T scene);

}
