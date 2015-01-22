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

import ch.awae.simtrack.Global;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * manages the translation between hex coordinates and screen coordinates, as
 * well as the hex scaling.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-17
 * @since SimTrack 0.0.1
 */
public class SceneViewPort {

	private int minZoom = 10;
	private int zoom;
	private Point sceneCorner;
	private Point sceneDimensions;
	private Point screenDimensions;

	public static final double SQRT3 = Math.sqrt(3);

	/**
	 * creates a new instance.
	 * 
	 * @param m
	 * @param w
	 */
	public SceneViewPort(Map m, Window w) {
		int hScreen = w.getWidth();
		int vScreen = w.getContentPane().getHeight() - 150; // TODO: fix bound
															// refs
		this.screenDimensions = new Point(hScreen, vScreen);
		{
			double minH = hScreen / (m.getHorizontalSize() - 1.0);
			if (minH > this.minZoom)
				this.minZoom = (int) Math.ceil(minH);
		}
		this.zoom = this.minZoom;
		this.sceneDimensions = new Point((m.getHorizontalSize() - 1) * 100,
				(int) ((m.getVerticalSize() - 1) * SQRT3 * 50));
		this.sceneCorner = new Point(0, 0);
		this.updateCorner();
	}

	/**
	 * sets the zoom level.
	 * 
	 * @param zoom
	 */
	public void zoom(int delta, int fixX, int fixY) {
		int newZoom = this.zoom + delta;
		if (newZoom < this.minZoom)
			newZoom = this.minZoom;
		int x = this.sceneCorner.x - fixX, y = this.sceneCorner.y - fixY;
		x *= newZoom;
		y *= newZoom;
		x /= this.zoom;
		y /= this.zoom;
		this.sceneCorner = new Point(x + fixX, y + fixY);
		this.zoom = newZoom;
		this.updateCorner();
	}

	/**
	 * gets the dimensions of the screen (only rendering part)
	 * 
	 * @return the screen dimensions
	 */
	public Point getScreenDimensions() {
		return this.screenDimensions;
	}

	private void updateCorner() {
		double minX = this.screenDimensions.x
				- (0.01 * this.zoom * this.sceneDimensions.x);
		double minY = this.screenDimensions.y
				- (0.01 * this.zoom * this.sceneDimensions.y);
		int x = this.sceneCorner.x;
		int y = this.sceneCorner.y;
		if (x > 0)
			x = 0;
		if (x < minX)
			x = (int) minX;
		if (y > 0)
			y = 0;
		if (y < minY)
			y = (int) minY;
		this.sceneCorner = new Point(x, y);
	}

	/**
	 * provides the inner radius of a hex tile in pixels. The inner radius
	 * corresponds to the distance between the hex tile centre and the centre of
	 * an edge.
	 * 
	 * @return the inner hex radius
	 */
	public int getZoom() {
		return this.zoom;
	}

	/**
	 * calculates the scene pixel coordinates of the centre of a given hex tile.
	 * The scene coordinates are not the screen coordinates.
	 * 
	 * @param hexCoor
	 *            the hex to convert to scene coordinates
	 * @return the position of the hex centre on the scene.
	 */
	public Point getScenePos(TileCoordinate hexCoor) {
		int x = (2 * hexCoor.getU() + hexCoor.getV()) * 50;
		int y = (int) (hexCoor.getV() * SQRT3 * 50);
		return new Point(x, y);
	}

	/**
	 * calculates the closest hex tile for any given scene coordinates.
	 * 
	 * @param p
	 * @return the hex closest to the given scene coordinates.
	 */
	public TileCoordinate getHexPos(Point p) {
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
		if (baseV < 0 || baseV >= Global.map.getVerticalSize())
			return null;

		return new TileCoordinate(baseU, baseV);

	}

	/**
	 * returns the screen coordinate for a given scene coordinate
	 * 
	 * @param p
	 *            the scene coordinate
	 * @return the screen coordinate
	 */
	public Point getScreenCoordinate(Point p) {
		double x = p.x;
		double y = p.y;
		x *= 0.01 * this.zoom;
		y *= 0.01 * this.zoom; // here : screen scaled scene coordinate
		x += this.sceneCorner.x;
		y += this.sceneCorner.y;
		return new Point((int) x, (int) y);
	}

	@SuppressWarnings("javadoc")
	public int getMinZoom() {
		return this.minZoom;
	}

	public void moveScene(int dx, int dy) {
		this.sceneCorner.x += dx;
		this.sceneCorner.y += dy;
		this.updateCorner();
	}

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p
	 *            the screen coordinate
	 * @return the scene coordinate
	 */
	public Point getSceneCoordinate(Point p) {
		double x = p.x - this.sceneCorner.x;
		double y = p.y - this.sceneCorner.y;
		x /= 0.01 * this.zoom;
		y /= 0.01 * this.zoom;
		return new Point((int) x, (int) y);
	}
}
