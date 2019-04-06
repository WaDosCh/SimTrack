package ch.awae.simtrack.scene.game.controller;

import java.awt.Dimension;
import java.awt.Point;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.input.Binding;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.view.ViewPort;

/**
 * Navigation Tool. Used for Scene movement & zoom
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class Navigator implements BaseTicker {

	private InputController input;
	private ViewPort viewPort;
	private Scene scene;

	private Binding A, S, D, W;

	/**
	 * instantiates a new navigator
	 */
	public Navigator(Scene scene, ViewPort viewPort, InputController input) {
		this.scene = scene;
		this.viewPort = viewPort;
		this.input = input;

		A = input.getBinding(InputAction.PAN_LEFT);
		S = input.getBinding(InputAction.PAN_DOWN);
		D = input.getBinding(InputAction.PAN_RIGHT);
		W = input.getBinding(InputAction.PAN_UP);

	}

	private final static int BORDER = 40;
	private final static float deltaZoom = -.05f;
	private boolean isActive = true;
	private final static int MOVE_SPEED = 8;

	/**
	 * indicates whether or not the navigator is active
	 * 
	 * @return {@code true} if and only if the navigator is active
	 */
	public boolean isEnabled() {
		return this.isActive;
	}

	/**
	 * set the activity state of the navigator
	 * 
	 * @param isEnabled
	 */
	public void setEnabled(boolean isEnabled) {
		this.isActive = isEnabled;
	}

	/**
	 * performs an update tick on the navigator
	 */
	@Override
	public void tick() {
		if (!this.isActive)
			return;
		Point mouse = input.getMousePosition();
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		Dimension size = this.scene.getScreenSize();
		if (mouse.x < BORDER || A.isPressed())
			dx = 1;
		if (mouse.y < BORDER || W.isPressed())
			dy = 1;
		if (mouse.x > size.width - BORDER || D.isPressed())
			dx = -1;
		if (mouse.y > size.height - BORDER || S.isPressed())
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		this.viewPort.moveScene(dx, dy);

		double amount = input.getScroll();
		if (amount != 0)
			this.viewPort.zoom((float) (amount * deltaZoom), mouse.x, mouse.y);
	}

}
