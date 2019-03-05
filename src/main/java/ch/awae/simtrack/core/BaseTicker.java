package ch.awae.simtrack.core;

public interface BaseTicker<T extends Scene<T>> extends NamedComponent {

	void tick(T scene);

}
