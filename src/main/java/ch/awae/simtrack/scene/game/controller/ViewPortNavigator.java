package ch.awae.simtrack.scene.game.controller;

import java.awt.Dimension;
import java.awt.Point;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.input.InputHandler;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.view.Design;
import ch.judos.generic.data.geometry.PointD;
import lombok.Getter;

/**
 * manages the translation between hex coordinates and screen coordinates, as well as the hex scaling.
 */
public class ViewPortNavigator implements BaseTicker, InputHandler {

	public static int outsideBounds = 0;
	private static double ZOOM_MIN = 0.1;
	private static final double ZOOM_MAX = 10;
	private static final double ZOOM_DEFAULT = 1;
	private final static double ZOOM_SPEED_FACTOR = 1.2;

	private final static int SCROLL_MOUSE_BORDER = 10;
	private final static int SCROLL_MOVE_SPEED = 8;
	private int dx = 0, dy = 0;

	private @Getter double zoom;
	private @Getter double targetZoom;

	private PointD sceneCorner;
	private SceneCoordinate sceneDimensions;
	private Point focusedPointForZoom;

	private @Getter Dimension screenSize;

	private Model model;
	private InputController input;

	public ViewPortNavigator(Model model, Dimension screenSize, InputController input) {
		this.model = model;
		this.screenSize = screenSize;
		this.input = input;
		this.resetZoomAndScrolling();
	}

	/**
	 * initialises the viewport
	 */
	public void resetZoomAndScrolling() {
		this.zoom = ZOOM_DEFAULT;
		this.targetZoom = this.zoom;
		this.sceneCorner = new PointD(0, 0);
		update();
	}

	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
		update();
	}

	public void update() {
		double minH = this.screenSize.width / (this.model.getHorizontalSize() - 1.0);
		if (minH > ZOOM_MIN)
			ZOOM_MIN = minH * 0.01;
		this.sceneDimensions = new SceneCoordinate((this.model.getHorizontalSize() - 1) * 100,
				((this.model.getVerticalSize() - 1) * Math.sqrt(3) * 50));
		updateCornerViewPortInsideMap();
	}

	/**
	 * returns the scene coordinate for a given screen coordinate
	 * 
	 * @param p the screen coordinate
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
	 * @param p the scene coordinate
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
		updateCornerViewPortInsideMap();
	}

	private void updateCornerViewPortInsideMap() {
		double minX = this.screenSize.width - (this.zoom * this.sceneDimensions.s);
		double minY = this.screenSize.height - Design.toolbarHeight - (this.zoom * this.sceneDimensions.t);
		double x = this.sceneCorner.x;
		double y = this.sceneCorner.y;
		if (x > outsideBounds)
			x = outsideBounds;
		if (x < minX - outsideBounds)
			x = (int) minX - outsideBounds;
		if (y > outsideBounds && y < minY - outsideBounds)
			y = minY / 2;
		else if (y > outsideBounds)
			y = outsideBounds;
		else if (y < minY - outsideBounds)
			y = (int) minY - outsideBounds;
		this.sceneCorner.setLocation(x, y);
	}

	/**
	 * update target zoom level. the given point remains stationary while zooming.
	 * 
	 * @param dzoom positive or negative, independent of value will update targetZoom by one step
	 * @param fixed fixed point
	 */
	public void zoom(double dzoom, Point fixed) {

		this.focusedPointForZoom = fixed;
		if (dzoom < 0)
			this.targetZoom *= ZOOM_SPEED_FACTOR;
		else if (dzoom > 0)
			this.targetZoom /= ZOOM_SPEED_FACTOR;

		if (this.targetZoom < ZOOM_MIN)
			this.targetZoom = ZOOM_MIN;
		if (this.targetZoom > ZOOM_MAX)
			this.targetZoom = ZOOM_MAX;
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
		updateCornerViewPortInsideMap();
	}

	/**
	 * focuses the given hex
	 * 
	 * @param hex the hex to be focused
	 * @param g the graphics
	 * @return a new graphics instance that has the centre of the given tile in its origin and a total tile width of 100
	 */
	public void focusHex(TileCoordinate hex, Graphics g) {
		Point p = toScreenCoordinate(hex.toSceneCoordinate());
		g.translate(p.x, p.y);
		g.scale(this.zoom, this.zoom);
	}

	/**
	 * transforms the screen coordinate system into the the scene coordinate system
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
	 * checks if a given area around a given scene coordinate is at least partially visible on the screen. The check can
	 * be performed as efficiently as possible and therefore may not be perfect. The implementation must avoid any false
	 * negatives, but may introduce false positives (i.e. a return value of {@code false} implies that the area is
	 * certainly invisible, but a return value of {@code true} does not imply any visibility)
	 * 
	 * @param point the scene coordinate
	 * @param radius the radius of the rectangle area to be checked
	 * @return false if it can be proven that the area is invisible, true otherwise
	 */
	public boolean isVisible(SceneCoordinate point, int radius) {
		Point screenPos = toScreenCoordinate(point);
		double screenRad = radius * this.zoom;
		// above or to the left is outside
		if (screenPos.x < -screenRad || screenPos.y < -screenRad)
			return false;
		// below or to the right is outside
		if (screenPos.x > screenSize.width + screenRad || screenPos.y > screenSize.height + screenRad)
			return false;
		// anything else is (potentially) inside
		return true;
	}

	/**
	 * checks if a given tile is (potentially visible)
	 * 
	 * @param hex the hex to check
	 * @return false if it can be proven that the tile is invisible, true otherwise
	 * @see #isVisible(Point, int)
	 */
	public boolean isVisible(TileCoordinate tileCoordinate) {
		// use default radius size of tiles (which is something below 60)
		return isVisible(tileCoordinate.toSceneCoordinate(), 60);
	}

	@Override
	public void tick() {
		if (this.model.getIsPaused().get())
			return;
		updateMovingMap();
		updateZoomFactor();
	}

	public Point getSceneCorner() {
		return this.sceneCorner.getPoint();
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isActionAndConsume(InputAction.PAN_LEFT)) {
			if (dx >= 0)
				dx = event.isPress() ? 1 : 0;
		}
		if (event.isActionAndConsume(InputAction.PAN_RIGHT)) {
			if (dx <= 0)
				dx = event.isPress() ? -1 : 0;
		}
		if (event.isActionAndConsume(InputAction.PAN_DOWN)) {
			if (dy <= 0)
				dy = event.isPress() ? -1 : 0;
		}
		if (event.isActionAndConsume(InputAction.PAN_UP)) {
			if (dy >= 0)
				dy = event.isPress() ? 1 : 0;
		}
		if (event.isChanged()) {
			double scrollAmount = event.getChangeValue();
			zoom(scrollAmount, event.getCurrentMousePosition());
		}
	}

	private void updateMovingMap() {
		Point mousePos = input.getMousePosition();
		if (mousePos == null)
			return;

		int mx = 0, my = 0;
		if (mousePos.x < SCROLL_MOUSE_BORDER)
			mx = 1;
		if (mousePos.y < SCROLL_MOUSE_BORDER)
			my = 1;
		if (mousePos.x > this.screenSize.width - SCROLL_MOUSE_BORDER)
			mx = -1;
		if (mousePos.y > this.screenSize.height - SCROLL_MOUSE_BORDER)
			my = -1;
		mx += dx;
		my += dy;
		mx *= SCROLL_MOVE_SPEED;
		my *= SCROLL_MOVE_SPEED;
		moveScene(mx, my);
	}

}
