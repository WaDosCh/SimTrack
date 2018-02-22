package ch.awae.simtrack.scene.game.view.renderer;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;

/**
 * General representation of any renderer.
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
@FunctionalInterface
public interface Renderer extends BaseRenderer<Game> {

	/**
	 * calls the {@link #render(Graphics2D, Game)} function with a new
	 * graphics instance such that transforms and strokes are not propagated
	 * back out
	 * 
	 * @param g
	 * @param view
	 */
	default void renderSafe(Graphics g, Game view) {
		Graphics.Stack stack = g.getStack();
		try {
			render(g, view);
		} finally {
			g.setStack(stack);
		}
	}

}
