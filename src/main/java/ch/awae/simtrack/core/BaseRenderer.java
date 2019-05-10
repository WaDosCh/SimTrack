package ch.awae.simtrack.core;

import ch.awae.simtrack.window.Graphics;

@FunctionalInterface
public interface BaseRenderer extends NamedComponent {

	void render(Graphics graphics);

}
