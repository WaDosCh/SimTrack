package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.awt.Point;

import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;
import lombok.Getter;

/**
 * manages the translation between hex coordinates and screen coordinates, as
 * well as the hex scaling.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
class ViewPort implements IViewPort {

	public static int outsideBounds = 100;

	private int minZoom = 10;
	private @Getter double zoom;

	@Getter
	private Point sceneCorner, screenDimensions;
	private SceneCoordinate sceneDimensions;
	private IGameView owner;

	/**
	 * instantiates a new view-port
	 * 
	 * @param owner
	 */
	ViewPort(IGameView owner) {
		this.owner = owner;
		this.init();
	}

	@Override
	public SceneCoordinate toSceneCoordinate(Point p) {
		double x = p.x - this.sceneCorner.x;
		double y = p.y - this.sceneCorner.y;
		x /= 0.01 * this.zoom;
		y /= 0.01 * this.zoom;
		return new SceneCoordinate(x, y);
	}

	@Override
	public Point toScreenCoordinate(SceneCoordinate p) {
		double x = p.s;
		double y = p.t;
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
		double minH = hScreen / (this.owner.getModel().getHorizontalSize() - 1.0);
		if (minH > this.minZoom)
			this.minZoom = (int) Math.ceil(minH);
		this.zoom = this.minZoom;
		this.sceneDimensions = new SceneCoordinate((this.owner.getModel().getHorizontalSize() - 1) * 100,
				((this.owner.getModel().getVerticalSize() - 1) * Math.sqrt(3) * 50));
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
		this.sceneCorner.x += (float) dx * (100. / this.zoom);
		this.sceneCorner.y += (float) dy * (100. / this.zoom);
		updateCorner();
	}

	private void updateCorner() {
		double minX = this.screenDimensions.x - (0.01 * this.zoom * this.sceneDimensions.s);
		double minY = this.screenDimensions.y - (0.01 * this.zoom * this.sceneDimensions.t);
		int x = this.sceneCorner.x;
		int y = this.sceneCorner.y;
		if (x > outsideBounds)
			x = outsideBounds;
		if (x < minX - outsideBounds)
			x = (int) minX - outsideBounds;
		if (y > outsideBounds)
			y = outsideBounds;
		if (y < minY - outsideBounds)
			y = (int) minY - outsideBounds;
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
		double newZoom = this.zoom + delta;
		if (newZoom < this.minZoom)
			newZoom = this.minZoom;
		int x = this.sceneCorner.x - fixX, y = this.sceneCorner.y - fixY;
		x *= newZoom / this.zoom;
		y *= newZoom / this.zoom;
		this.sceneCorner = new Point(x + fixX, y + fixY);
		this.zoom = newZoom;
		updateCorner();
	}

	@Override
	public Graphics2D focusHex(TileCoordinate hex, Graphics2D g) {
		Point p = toScreenCoordinate(hex.toSceneCoordinate());
		double zoomFac = 0.01 * this.zoom;
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(p.x, p.y);
		g2.scale(zoomFac, zoomFac);
		return g2;
	}

	@Override
	public Graphics2D transformToScene(Graphics2D g) {
		return transformToScene(g, new SceneCoordinate(0, 0));
	}

	public Graphics2D transformToScene(Graphics2D g, SceneCoordinate sceneCoordinates) {
		Point p = toScreenCoordinate(new SceneCoordinate(0, 0));
		double zoomFac = 0.01 * this.zoom;
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(p.x, p.y);
		g2.scale(zoomFac, zoomFac);
		return g2;
	}

	@Override
	public boolean isVisible(SceneCoordinate point, int radius) {
		Point screenPos = toScreenCoordinate(point);
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
	public boolean isVisible(TileCoordinate tileCoordinate) {
		return isVisible(tileCoordinate.toSceneCoordinate(), 80);
	}

}
