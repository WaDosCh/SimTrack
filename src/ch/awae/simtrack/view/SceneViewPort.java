/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.view;

import java.awt.Point;

import ch.awae.simtrack.HighLogic;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * manages the translation between hex coordinates and screen coordinates, as
 * well as the hex scaling.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class SceneViewPort {

	private static int minZoom = 10, zoom;
	private static Point sceneCorner, sceneDimensions, screenDimensions;

	public static final double SQRT3 = Math.sqrt(3);

	/**
	 * calculates the closest hex tile for any given scene coordinates.
	 * 
	 * @param p
	 * @return the hex closest to the given scene coordinates.
	 */
	public static TileCoordinate getHexPos(Point p) {
		double v = (2.0 * p.y) / (SQRT3 * 100);
		double u = (1.0 * p.x) / 100 - v / 2;
		int baseU = (int) Math.floor(u);
		int baseV = (int) Math.floor(v);
		u -= baseU;
		v -= baseV;

		if ((v < 1 - (2 * u)) && (v < (1 - u) / 2)) {
			// aok
		} else if ((v > 2 - (2 * u)) && (v > 1 - (u / 2))) {
			baseU++;
			baseV++;
		} else if (v < u) {
			baseU++;
		} else {
			baseV++;
		}

		// note: horizontal validation irrelevant, since could NEVER be reached
		// due to zoom limitations along the horizontal axis.
		if (baseV < 0 || baseV >= HighLogic.map.getVerticalSize())
			return null;

		return new TileCoordinate(baseU, baseV);

	}

	public static int getMinZoom() {
		return minZoom;
	}

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p
	 *            the screen coordinate
	 * @return the scene coordinate
	 */
	public static Point getSceneCoordinate(Point p) {
		double x = p.x - sceneCorner.x;
		double y = p.y - sceneCorner.y;
		x /= 0.01 * zoom;
		y /= 0.01 * zoom;
		return new Point((int) x, (int) y);
	}

	/**
	 * calculates the scene pixel coordinates of the centre of a given hex tile.
	 * The scene coordinates are not the screen coordinates.
	 * 
	 * @param hexCoor
	 *            the hex to convert to scene coordinates
	 * @return the position of the hex centre on the scene.
	 */
	public static Point getScenePos(TileCoordinate hexCoor) {
		int x = (2 * hexCoor.getU() + hexCoor.getV()) * 50;
		int y = (int) (hexCoor.getV() * SQRT3 * 50);
		return new Point(x, y);
	}

	/**
	 * returns the screen coordinate for a given scene coordinate
	 * 
	 * @param p
	 *            the scene coordinate
	 * @return the screen coordinate
	 */
	public static Point getScreenCoordinate(Point p) {
		double x = p.x;
		double y = p.y;
		x *= 0.01 * zoom;
		y *= 0.01 * zoom; // here : screen scaled scene coordinate
		x += sceneCorner.x;
		y += sceneCorner.y;
		return new Point((int) x, (int) y);
	}

	/**
	 * gets the dimensions of the screen (only rendering part)
	 * 
	 * @return the screen dimensions
	 */
	public static Point getScreenDimensions() {
		return screenDimensions;
	}

	/**
	 * provides the inner radius of a hex tile in pixels. The inner radius
	 * corresponds to the distance between the hex tile centre and the centre of
	 * an edge.
	 * 
	 * @return the inner hex radius
	 */
	public static int getZoom() {
		return zoom;
	}

	public static void init(Map m, Window w) {
		int hScreen = w.getWidth();
		int vScreen = w.getContentPane().getHeight() - 150;
		screenDimensions = new Point(hScreen, vScreen);
		double minH = hScreen / (m.getHorizontalSize() - 1.0);
		if (minH > minZoom)
			minZoom = (int) Math.ceil(minH);
		zoom = minZoom;
		sceneDimensions = new Point((m.getHorizontalSize() - 1) * 100,
				(int) ((m.getVerticalSize() - 1) * SQRT3 * 50));
		sceneCorner = new Point(0, 0);
		updateCorner();
	}

	public static void moveScene(int dx, int dy) {
		sceneCorner.x += dx;
		sceneCorner.y += dy;
		updateCorner();
	}

	private static void updateCorner() {
		double minX = screenDimensions.x - (0.01 * zoom * sceneDimensions.x);
		double minY = screenDimensions.y - (0.01 * zoom * sceneDimensions.y);
		int x = sceneCorner.x;
		int y = sceneCorner.y;
		if (x > 0)
			x = 0;
		if (x < minX)
			x = (int) minX;
		if (y > 0)
			y = 0;
		if (y < minY)
			y = (int) minY;
		sceneCorner = new Point(x, y);
	}

	/**
	 * sets the zoom level.
	 * 
	 * @param delta
	 *            difference between new zoom and current
	 * @param fixX
	 *            x-coordinate of the fixed point
	 * @param fixY
	 *            y-coordinate of the fixed point
	 */
	public static void zoom(int delta, int fixX, int fixY) {
		int newZoom = zoom + delta;
		if (newZoom < minZoom)
			newZoom = minZoom;
		int x = sceneCorner.x - fixX, y = sceneCorner.y - fixY;
		x *= newZoom;
		y *= newZoom;
		x /= zoom;
		y /= zoom;
		sceneCorner = new Point(x + fixX, y + fixY);
		zoom = newZoom;
		updateCorner();
	}
}
