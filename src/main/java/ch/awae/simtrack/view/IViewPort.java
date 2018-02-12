package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.awt.Point;

import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * basic viewport interface
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public interface IViewPort {

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p
	 *            the screen coordinate
	 * @return the scene coordinate
	 */
	public SceneCoordinate toSceneCoordinate(Point p);

	/**
	 * returns the screen coordinate for a given scene coordinate
	 * 
	 * @param p
	 *            the scene coordinate
	 * @return the screen coordinate
	 */
	public Point toScreenCoordinate(SceneCoordinate p);

	/**
	 * focuses the given hex
	 * 
	 * @param hex
	 *            the hex to be focused
	 * @param g
	 *            the graphics
	 * @return a new graphics instance that has the centre of the given tile in
	 *         its origin and a total tile width of 100
	 */
	public Graphics2D focusHex(TileCoordinate hex, Graphics2D g);

	/**
	 * returns the dimension of the section of the drawing surface reserved for
	 * the scene rendering.
	 * 
	 * @return the scene surface dimensions
	 */
	public Point getScreenDimensions();

	/**
	 * checks if a given area around a given scene coordinate is at least
	 * partially visible on the screen. The check can be performed as
	 * efficiently as possible and therefore may not be perfect. The
	 * implementation must avoid any false negatives, but may introduce false
	 * positives (i.e. a return value of {@code false} implies that the area is
	 * certainly invisible, but a return value of {@code true} does not imply
	 * any visibility)
	 * 
	 * @param point
	 *            the scene coordinate
	 * @param radius
	 *            the radius of the area to be checked
	 * @return false if it can be proven that the area is invisible, true
	 *         otherwise
	 */
	public boolean isVisible(SceneCoordinate point, int radius);

	/**
	 * checks if a given tile is (potentially visible)
	 * 
	 * @param hex
	 *            the hex to check
	 * @return false if it can be proven that the tile is invisible, true
	 *         otherwise
	 * @see #isVisible(Point, int)
	 */
	public boolean isVisible(TileCoordinate hex);

	public int getZoom();

	public Point getSceneCorner();

	/**
	 * transforms the screen coordinate system into the the scene coordinate
	 * system
	 * 
	 * @param g
	 */
	public Graphics2D transformToScene(Graphics2D g);

}