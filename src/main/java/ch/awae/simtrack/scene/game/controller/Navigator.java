package ch.awae.simtrack.scene.game.controller;

import java.awt.Dimension;
import java.awt.Point;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.input.InputHandler;
import ch.awae.simtrack.scene.game.view.ViewPort;

/**
 * Navigation Tool. Used for Scene movement & zoom
 */
public class Navigator implements BaseTicker, InputHandler {

	private final static int BORDER = 40;
	private final static float deltaZoom = -.05f;
	private final static int MOVE_SPEED = 8;

	private boolean isActive = true;
	private InputController input;
	private ViewPort viewPort;
	private Scene scene;
	private int dx = 0, dy = 0;
	private double scrollAmount = 0;

	public Navigator(Scene scene, ViewPort viewPort, InputController input) {
		this.scene = scene;
		this.viewPort = viewPort;
		this.input = input;
	}

	public boolean isEnabled() {
		return this.isActive;
	}

	public void setEnabled(boolean isEnabled) {
		this.isActive = isEnabled;
	}

	@Override
	public void tick() {
		if (!this.isActive)
			return;
		Point mouse = input.getMousePosition();
		if (mouse == null)
			return;

		Dimension size = this.scene.getScreenSize();
		int mx = 0, my = 0;
		if (mouse.x < BORDER)
			mx = 1;
		if (mouse.y < BORDER)
			my = 1;
		if (mouse.x > size.width - BORDER)
			mx = -1;
		if (mouse.y > size.height - BORDER)
			my = -1;
		mx += dx;
		my += dy;
		mx *= MOVE_SPEED;
		my *= MOVE_SPEED;
		this.viewPort.moveScene(mx, my);

		if (this.scrollAmount != 0) {
			this.viewPort.zoom((float) (scrollAmount * deltaZoom), mouse.x, mouse.y);
			this.scrollAmount = 0;
		}
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isActionAndConsume(InputAction.PAN_LEFT)) {
			dx = event.isPress() ? 1 : 0;
		}
		if (event.isActionAndConsume(InputAction.PAN_RIGHT)) {
			dx = event.isPress() ? -1 : 0;
		}
		if (event.isActionAndConsume(InputAction.PAN_DOWN)) {
			dy = event.isPress() ? -1 : 0;
		}
		if (event.isActionAndConsume(InputAction.PAN_UP)) {
			dy = event.isPress() ? 1 : 0;
		}
		if (event.isChanged()) {
			this.scrollAmount += event.getChangeValue();
		}
	}

}
