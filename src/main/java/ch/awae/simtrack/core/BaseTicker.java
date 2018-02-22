package ch.awae.simtrack.core;

public interface BaseTicker<T extends Scene<T>> extends BaseComponent<T> {

	void tick(T scene);

}
