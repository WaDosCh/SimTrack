package ch.awae.simtrack.scene;

import ch.awae.simtrack.view.Graphics;

@FunctionalInterface
public interface BaseRenderer<T extends Scene<T>> {

	void render(Graphics graphics, T scene);

}
