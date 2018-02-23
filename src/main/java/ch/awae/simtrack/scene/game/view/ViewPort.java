package ch.awae.simtrack.scene.game.view;

import java.awt.Point;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.judos.generic.data.geometry.PointD;
import ch.judos.generic.data.geometry.PointI;
import lombok.Getter;

/**
 * manages the translation between hex coordinates and screen coordinates, as
 * well as the hex scaling.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class ViewPort {

	public static int outsideBounds = 100;

	private double minZoom = 0.1;
	private double maxZoom = 10;
	private double defaultZoom = 1;
	private @Getter double zoom;
	private @Getter double targetZoom;

	private PointD sceneCorner;

	private PointI screenDimensions;
	private SceneCoordinate sceneDimensions;
	private Game owner;

	private PointI focusedPointForZoom;

	/**
	 * instantiates a new view-port
	 * 
	 * @param owner
	 */
	public ViewPort(Game owner) {
		this.owner = owner;
		this.init();
	}

	/**
	 * initialises the viewport
	 */
	void init() {
		int hScreen = this.owner.getHorizontalScreenSize();
		int vScreen = this.owner.getVerticalScreenSize() - 150;
		this.screenDimensions = new PointI(hScreen, vScreen);
		double minH = hScreen / (this.owner.getModel().getHorizontalSize() - 1.0);
		if (minH > this.minZoom)
			this.minZoom = minH * 0.01;

		this.zoom = this.defaultZoom;
		this.targetZoom = this.zoom;
		this.sceneDimensions = new SceneCoordinate((this.owner.getModel().getHorizontalSize() - 1) * 100,
				((this.owner.getModel().getVerticalSize() - 1) * Math.sqrt(3) * 50));
		this.sceneCorner = new PointD(0, 0);
		updateCorner();
	}
	
	public void update() {
		int hScreen = this.owner.getHorizontalScreenSize();
		int vScreen = this.owner.getVerticalScreenSize() - 150;
		this.screenDimensions = new PointI(hScreen, vScreen);
		double minH = hScreen / (this.owner.getModel().getHorizontalSize() - 1.0);
		if (minH > this.minZoom)
			this.minZoom = minH * 0.01;
		this.sceneDimensions = new SceneCoordinate((this.owner.getModel().getHorizontalSize() - 1) * 100,
				((this.owner.getModel().getVerticalSize() - 1) * Math.sqrt(3) * 50));
		updateCorner();
	}

	/**
	 * returns the dimension of the section of the drawing surface reserved for
	 * the scene rendering.
	 * 
	 * @return the scene surface dimensions
	 */
	public Point getScreenDimensions() {
		return this.screenDimensions;
	}

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p
	 *            the screen coordinate
	 * @return the scene coordinate
	 */
	public SceneCoordinate toSceneCoordinate(Point p) {
		double x = p.x - this.sceneCorner.x;
		double y = p.y - this.sceneCorner.y;
		x /= this.zoom;
		y /= this.zoom;
		return new SceneCoordinate(x, y);
	}

	/**
	 * returns the screen coordinate for a given scene coordinate
	 * 
	 * @param p
	 *            the scene coordinate
	 * @return the screen coordinate
	 */
	public Point toScreenCoordinate(SceneCoordinate p) {
		double x = p.s;
		double y = p.t;
		x *= this.zoom;
		y *= this.zoom; // here : screen scaled scene coordinate
		x += this.sceneCorner.x;
		y += this.sceneCorner.y;
		return new Point((int) x, (int) y);
	}

	/**
	 * moves the scene
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveScene(int dx, int dy) {
		this.sceneCorner.x += (float) dx;
		this.sceneCorner.y += (float) dy;
		updateCorner();
	}

	private void updateCorner() {
		double minX = this.screenDimensions.x - (this.zoom * this.sceneDimensions.s);
		double minY = this.screenDimensions.y - (this.zoom * this.sceneDimensions.t);
		double x = this.sceneCorner.x;
		double y = this.sceneCorner.y;
		if (x > outsideBounds)
			x = outsideBounds;
		if (x < minX - outsideBounds)
			x = (int) minX - outsideBounds;
		if (y > outsideBounds)
			y = outsideBounds;
		if (y < minY - outsideBounds)
			y = (int) minY - outsideBounds;
		this.sceneCorner = new PointD(x, y);
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
	public void zoom(int delta, int fixX, int fixY) {
		this.focusedPointForZoom = new PointI(fixX, fixY);
		if (delta > 0)
			this.targetZoom *= 1.2;
		if (delta < 0)
			this.targetZoom /= 1.2;

		if (this.targetZoom < this.minZoom)
			this.targetZoom = this.minZoom;
		if (this.targetZoom > this.maxZoom)
			this.targetZoom = this.maxZoom;
	}

	protected void updateZoomFactor() {
		if (this.focusedPointForZoom == null)
			return;
		double diff = (this.targetZoom - this.zoom);
		double diffDir = Math.signum(diff);
		diff = Math.abs(diff);
		double diffChange = diff * 0.1;
		if (diffChange > 0 && diffChange < 0.001 * this.targetZoom) {
			diffChange = Math.min(0.001 * this.targetZoom, diff);
		}
		double newZoom = this.zoom + diffDir * diffChange;

		PointD scroll = this.sceneCorner.subtract(this.focusedPointForZoom);
		scroll.scaleI(newZoom / this.zoom);
		this.sceneCorner = scroll.add(this.focusedPointForZoom);

		this.zoom = newZoom;
		updateCorner();
	}

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
	public void focusHex(TileCoordinate hex, Graphics g) {
		Point p = toScreenCoordinate(hex.toSceneCoordinate());
		g.translate(p.x, p.y);
		g.scale(this.zoom, this.zoom);
	}

	/**
	 * transforms the screen coordinate system into the the scene coordinate
	 * system
	 * 
	 * @param g
	 */
	public void transformToScene(Graphics g) {
		transformToScene(g, new SceneCoordinate(0, 0));
	}

	public void transformToScene(Graphics g, SceneCoordinate sceneCoordinates) {
		Point p = toScreenCoordinate(new SceneCoordinate(0, 0));
		g.translate(p.x, p.y);
		g.scale(this.zoom, this.zoom);
	}

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
	public boolean isVisible(SceneCoordinate point, int radius) {
		Point screenPos = toScreenCoordinate(point);
		double screenRad = radius * this.zoom;
		// above or to the left is outside
		if (screenPos.x < -screenRad || screenPos.y < -screenRad)
			return false;
		// below or to the right is outside
		if (screenPos.x > screenDimensions.x + screenRad || screenPos.y > screenDimensions.y + screenRad)
			return false;
		// anything else is (potentially) inside
		return true;
	}

	/**
	 * checks if a given tile is (potentially visible)
	 * 
	 * @param hex
	 *            the hex to check
	 * @return false if it can be proven that the tile is invisible, true
	 *         otherwise
	 * @see #isVisible(Point, int)
	 */
	public boolean isVisible(TileCoordinate tileCoordinate) {
		return isVisible(tileCoordinate.toSceneCoordinate(), 80);
	}

	public void tick() {
		updateZoomFactor();
	}

	public Point getSceneCorner() {
		return this.sceneCorner.getPoint();
	}

	public void reloadBounds() {
		init();
	}

}
