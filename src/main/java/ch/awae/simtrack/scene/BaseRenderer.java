package ch.awae.simtrack.scene;

@FunctionalInterface
public interface BaseRenderer<T extends Scene<T>> {

	void render(Graphics graphics, T scene);

}
