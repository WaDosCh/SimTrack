package ch.awae.simtrack.core;

@FunctionalInterface
public interface BaseRenderer<T extends Scene<T>> {

	void render(Graphics graphics, T scene);

}
