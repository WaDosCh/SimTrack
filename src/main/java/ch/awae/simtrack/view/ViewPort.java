package ch.awae.simtrack.view;

import java.awt.Point;

import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;
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
public class ViewPort implements IViewPort {

	public static int outsideBounds = 100;

	private double minZoom = 0.1;
	private double defaultZoom = 1;
	private @Getter double zoom;
	private @Getter double targetZoom;

	private PointD sceneCorner;

	private @Getter PointI screenDimensions;
	private SceneCoordinate sceneDimensions;
	private IGameView owner;

	private PointI focusedPointForZoom;

	/**
	 * instantiates a new view-port
	 * 
	 * @param owner
	 */
	ViewPort(IGameView owner) {
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

	@Override
	public SceneCoordinate toSceneCoordinate(Point p) {
		double x = p.x - this.sceneCorner.x;
		double y = p.y - this.sceneCorner.y;
		x /= this.zoom;
		y /= this.zoom;
		return new SceneCoordinate(x, y);
	}

	@Override
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
	void moveScene(int dx, int dy) {
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
	void zoom(int delta, int fixX, int fixY) {
		this.focusedPointForZoom = new PointI(fixX, fixY);
		if (delta > 0)
			this.targetZoom *= 1.3;
		if (delta < 0)
			this.targetZoom /= 1.3;

		if (this.targetZoom < this.minZoom)
			this.targetZoom = this.minZoom;
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

	@Override
	public void focusHex(TileCoordinate hex, Graphics g) {
		Point p = toScreenCoordinate(hex.toSceneCoordinate());
		g.translate(p.x, p.y);
		g.scale(this.zoom, this.zoom);
	}

	@Override
	public void transformToScene(Graphics g) {
		transformToScene(g, new SceneCoordinate(0, 0));
	}

	public void transformToScene(Graphics g, SceneCoordinate sceneCoordinates) {
		Point p = toScreenCoordinate(new SceneCoordinate(0, 0));
		g.translate(p.x, p.y);
		g.scale(this.zoom, this.zoom);
	}

	@Override
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

	@Override
	public boolean isVisible(TileCoordinate tileCoordinate) {
		return isVisible(tileCoordinate.toSceneCoordinate(), 80);
	}

	public void tick() {
		updateZoomFactor();
	}

	@Override
	public Point getSceneCorner() {
		return this.sceneCorner.getPoint();
	}

}
