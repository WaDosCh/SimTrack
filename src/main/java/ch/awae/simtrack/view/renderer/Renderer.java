package ch.awae.simtrack.view.renderer;

import java.awt.Graphics2D;

import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.GameView;

/**
 * General representation of any renderer.
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
@FunctionalInterface
public interface Renderer {

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
		render(g, view);
		g.setStack(stack);
	}

	/**
	 * The main rendering function. This will be called by the appropriate
	 * controller whenever the renderer should be active.
	 * 
	 * Transforms usually do not need to be managed, as it is recommended to
	 * always call a Renderer through the
	 * {@link #renderSafe(Graphics2D, GameView)} method
	 * 
	 * @param g
	 *            the graphics object to render with. This instance can be
	 *            transformed without limitations.
	 * @param view
	 *            the view that invokes the rendering
	 */
	public void render(Graphics g, GameView view);

}
