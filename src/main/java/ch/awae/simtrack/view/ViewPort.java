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

import java.awt.Graphics2D;
import java.awt.Point;

import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * manages the translation between hex coordinates and screen coordinates, as
 * well as the hex scaling.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
class ViewPort implements IViewPort {

	private int minZoom = 10, zoom;
	private Point sceneCorner, screenDimensions;
	private SceneCoordinate sceneDimensions;
	private static final double SQRT3 = Math.sqrt(3);
	private IView owner;

	/**
	 * instantiates a new view-port
	 * 
	 * @param owner
	 */
	ViewPort(IView owner) {
		this.owner = owner;
		this.init();
	}

	@Override
	public TileCoordinate toHex(SceneCoordinate p) {
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
		if (baseV < 0 || baseV >= this.owner.getModel().getVerticalSize())
			return null;

		return new TileCoordinate(baseU, baseV);

	}

	@Override
	public SceneCoordinate toScene(Point p) {
		double x = p.x - this.sceneCorner.x;
		double y = p.y - this.sceneCorner.y;
		x /= 0.01 * this.zoom;
		y /= 0.01 * this.zoom;
		return new SceneCoordinate(x, y);
	}

	@Override
	public SceneCoordinate toScene(TileCoordinate hexCoor) {
		double x = (2 * hexCoor.getU() + hexCoor.getV()) * 50;
		double y = hexCoor.getV() * SQRT3 * 50;
		return new SceneCoordinate(x, y);
	}

	@Override
	public Point toScreen(SceneCoordinate p) {
		double x = p.x;
		double y = p.y;
		x *= 0.01 * this.zoom;
		y *= 0.01 * this.zoom; // here : screen scaled scene coordinate
		x += this.sceneCorner.x;
		y += this.sceneCorner.y;
		return new Point((int) x, (int) y);
	}

	/**
	 * initialises the viewport
	 */
	void init() {
		int hScreen = this.owner.getHorizontalScreenSize();
		int vScreen = this.owner.getVerticalScreenSize() - 150;
		this.screenDimensions = new Point(hScreen, vScreen);
		double minH = hScreen
				/ (this.owner.getModel().getHorizontalSize() - 1.0);
		if (minH > this.minZoom)
			this.minZoom = (int) Math.ceil(minH);
		this.zoom = this.minZoom;
		this.sceneDimensions = new SceneCoordinate((this.owner.getModel()
				.getHorizontalSize() - 1) * 100,  ((this.owner.getModel()
				.getVerticalSize() - 1) * SQRT3 * 50));
		this.sceneCorner = new Point(0, 0);
		updateCorner();
	}

	/**
	 * moves the scene
	 * 
	 * @param dx
	 * @param dy
	 */
	void moveScene(int dx, int dy) {
		this.sceneCorner.x += dx;
		this.sceneCorner.y += dy;
		updateCorner();
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
	 * sets the zoom level.
	 * 
	 * @param delta
	 *            difference between new zoom and current
	 * @param fixX
	 *            x-coordinate of the fixed point
	 * @param fixY
	 *            y-coordinate of the fixed point
	 */
	void zoom(int delta, int fixX, int fixY) {
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
		updateCorner();
	}

	@Override
	public Graphics2D focusHex(TileCoordinate hex, Graphics2D g) {
		Point p = toScreen(hex);
		double zoomFac = 0.01 * this.zoom;
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(p.x, p.y);
		g2.scale(zoomFac, zoomFac);
		return g2;
	}

	@Override
	public Point getScreenDimensions() {
		return this.screenDimensions;
	}

	@Override
	public boolean isVisible(SceneCoordinate point, int radius) {
		Point screenPos = toScreen(point);
		double screenRad = radius * 0.01 * this.zoom;
		// above or to the left is outside
		if (screenPos.x < -screenRad || screenPos.y < -screenRad)
			return false;
		// below or to the right is outside
		if (screenPos.x > screenDimensions.x + screenRad || screenPos.y > screenDimensions.y + screenRad)
			return false;
		// anything else is (potentially) inside
		return true;
	}

	@Override
	public boolean isVisible(TileCoordinate hex) {
		return isVisible(toScene(hex), 60);
	}
}
