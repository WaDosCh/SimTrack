package ch.awae.simtrack.scene.game.view.renderer;

import ch.awae.simtrack.scene.BaseRenderer;
import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.game.view.GameView;

/**
 * General representation of any renderer.
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
@FunctionalInterface
public interface Renderer extends BaseRenderer<GameView> {

	/**
	 * calls the {@link #render(Graphics2D, GameView)} function with a new
	 * graphics instance such that transforms and strokes are not propagated
	 * back out
	 * 
	 * @param g
	 * @param view
	 */
	default void renderSafe(Graphics g, GameView view) {
		Graphics.Stack stack = g.getStack();
		try {
			render(g, view);
		} finally {
			g.setStack(stack);
		}
	}

}
