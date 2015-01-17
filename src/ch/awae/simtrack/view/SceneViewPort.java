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

	private int maxZoom = 50;
	private int minZoom = 10;
	private int zoom;
	private Point sceneCenterOnScreen;
	private Point sceneDimensions;
	private Point screenDimensions;

	private static final double SQRT3 = Math.sqrt(3);

	/**
	 * creates a new instance.
	 * 
	 * @param m
	 * @param w
	 */
	public SceneViewPort(Map m, Window w) {
		int hScreen = w.getWidth();
		int vScreen = w.getHeight();
		this.screenDimensions = new Point(hScreen, vScreen);
		{
			double minH = hScreen / (m.getHorizontalSize() - 1.0);
			if (minH > this.minZoom)
				this.minZoom = (int) Math.ceil(minH);
		}
		this.zoom = this.minZoom;
		this.sceneDimensions = new Point((m.getHorizontalSize() - 1)
				* this.maxZoom, (int) (m.getVerticalSize() * SQRT3
				* this.maxZoom / 2));
		this.sceneCenterOnScreen = new Point(hScreen / 2, vScreen / 2);
	}

	/**
	 * sets the zoom level.
	 * 
	 * @param zoom
	 */
	public void setZoom(int zoom) {
		this.zoom = Math.min(this.maxZoom, Math.max(this.minZoom, zoom));
		this.updateCentre();
	}

	/**
	 * sets the centre of the scene to the given point.
	 * 
	 * @param centre
	 *            the new position.
	 */
	public void setSceneCentre(Point centre) {
		this.sceneCenterOnScreen = centre.getLocation();
		this.updateCentre();
	}

	private void updateCentre() {
		double xmin = this.zoom * this.sceneDimensions.x / (2.0 * this.maxZoom);
		double ymin = this.zoom
				* Math.max(this.sceneDimensions.y, this.sceneDimensions.x
						* this.screenDimensions.y / this.screenDimensions.x)
				/ (2.0 * this.maxZoom);
		double xmax = this.screenDimensions.x - xmin;
		double ymax = this.screenDimensions.y - ymin;

		double x = Math.min(xmax, Math.max(xmin, this.sceneCenterOnScreen.x));
		double y = Math.min(ymax, Math.max(ymin, this.sceneCenterOnScreen.y));
		this.sceneCenterOnScreen = new Point((int) x, (int) y);
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
		int x = (2 * hexCoor.getU() + hexCoor.getV()) * this.maxZoom / 2;
		int y = (int) ((hexCoor.getU() * SQRT3 * this.maxZoom / 2));
		return new Point(x, y);
	}

	/**
	 * calculates the closest hex tile for any given scene coordinates.
	 * 
	 * @param p
	 * @return the hex closest to the given scene coordinates.
	 */
	public TileCoordinate getHexPos(Point p) {
		double v = (2.0 * p.y) / (SQRT3 * this.maxZoom);
		double u = (1.0 * p.x) / this.maxZoom - v / 2;

		int U = (int) Math.round(u);
		int V = (int) Math.round(v);

		return new TileCoordinate(U, V);
	}

	/**
	 * returns the screen coordinate for a given scene coordinate
	 * 
	 * @param p
	 *            the scene coordinate
	 * @return the screen coordinate
	 */
	public Point getScreenCoordinate(Point p) {
		double x = p.x - (this.sceneDimensions.x / 2.0);
		double y = p.y - (this.sceneDimensions.y / 2.0);
		x *= this.zoom;
		y *= this.zoom;
		x /= this.maxZoom;
		y /= this.maxZoom;
		x += this.sceneCenterOnScreen.x;
		y += this.sceneCenterOnScreen.y;
		return new Point((int) x, (int) y);
	}

	@SuppressWarnings("javadoc")
	public int getMaxZoom() {
		return this.maxZoom;
	}

	@SuppressWarnings("javadoc")
	public int getMinZoom() {
		return this.minZoom;
	}

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p
	 *            the screen coordinate
	 * @return the scene coordinate
	 */
	public Point getSceneCoordinate(Point p) {
		double x = p.x - this.sceneCenterOnScreen.x;
		double y = p.y - this.sceneCenterOnScreen.y;
		x *= this.maxZoom;
		y *= this.maxZoom;
		x /= this.zoom;
		y /= this.zoom;
		x += this.sceneDimensions.x / 2.0;
		y += this.sceneDimensions.y / 2.0;
		return new Point((int) x, (int) y);
	}
}
