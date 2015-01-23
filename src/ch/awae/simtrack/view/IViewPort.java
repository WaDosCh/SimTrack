package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.awt.Point;

import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IViewPort {

	/**
	 * calculates the closest hex tile for any given scene coordinates.
	 * 
	 * @param p
	 * @return the hex closest to the given scene coordinates.
	 */
	public TileCoordinate getHexPos(Point p);

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p
	 *            the screen coordinate
	 * @return the scene coordinate
	 */
	public Point getSceneCoordinate(Point p);

	/**
	 * calculates the scene pixel coordinates of the centre of a given hex tile.
	 * The scene coordinates are not the screen coordinates.
	 * 
	 * @param hexCoor
	 *            the hex to convert to scene coordinates
	 * @return the position of the hex centre on the scene.
	 */
	public Point getScenePos(TileCoordinate hexCoor);

	/**
	 * returns the screen coordinate for a given scene coordinate
	 * 
	 * @param p
	 *            the scene coordinate
	 * @return the screen coordinate
	 */
	public Point getScreenCoordinate(Point p);

	public Graphics2D focusHex(TileCoordinate hex, Graphics2D g);

	public Point getScreenDimensions();
	
}