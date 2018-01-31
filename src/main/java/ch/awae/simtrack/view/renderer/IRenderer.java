package ch.awae.simtrack.view.renderer;

import java.awt.Graphics2D;

import ch.awae.simtrack.view.IGameView;

/**
 * General representation of any renderer.
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
@FunctionalInterface
public interface IRenderer {

	/**
	 * calls the {@link #render(Graphics2D, IGameView)} function with a new
	 * graphics instance such that transforms and strokes are not propagated
	 * back out
	 * 
	 * @param g
	 * @param view
	 */
	default void renderSafe(Graphics2D g, IGameView view) {
		render((Graphics2D) g.create(), view);
	}

	/**
	 * The main rendering function. This will be called by the appropriate
	 * controller whenever the renderer should be active.
	 * 
	 * Transforms usually do not need to be managed, as it is recommended to
	 * always call a Renderer through the
	 * {@link #renderSafe(Graphics2D, IGameView)} method
	 * 
	 * @param g
	 *            the graphics object to render with. This instance can be
	 *            transformed without limitations.
	 * @param view
	 *            the view that invokes the rendering
	 */
	public void render(Graphics2D g, IGameView view);

}
