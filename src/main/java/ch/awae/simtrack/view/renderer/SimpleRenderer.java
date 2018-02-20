package ch.awae.simtrack.view.renderer;

import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.GameView;

@FunctionalInterface
public interface SimpleRenderer extends Renderer {

	@Override
	default void render(Graphics g, GameView view) {
		render(g);
	}
	
	void render(Graphics g);
	
}
