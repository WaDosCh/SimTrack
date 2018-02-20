package ch.awae.simtrack.view.renderer;

import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;

@FunctionalInterface
public interface SimpleRenderer extends Renderer {

	@Override
	default void render(Graphics g, IGameView view) {
		render(g);
	}
	
	void render(Graphics g);
	
}
