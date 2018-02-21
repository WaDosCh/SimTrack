package ch.awae.simtrack.controller;

import java.awt.Point;

import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.controller.input.Binding;
import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.scene.BaseTicker;
import ch.awae.simtrack.view.GameView;

/**
 * Navigation Tool. Used for Scene movement & zoom
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class Navigator implements BaseTicker<GameView> {

	private Input input;
	private GameView gameView;

	private Binding A, S, D, W;

	/**
	 * instantiates a new navigator
	 */
	public Navigator(GameView gameView, Input input) {
		this.gameView = gameView;
		this.input = input;

		A = input.getBinding(Action.PAN_LEFT);
		S = input.getBinding(Action.PAN_DOWN);
		D = input.getBinding(Action.PAN_RIGHT);
		W = input.getBinding(Action.PAN_UP);

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
	public void tick() {
		if (!this.isActive)
			return;
		Point mouse = input.getMousePosition();
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		if (mouse.x < BORDER || A.isPressed())
			dx = 1;
		if (mouse.y < BORDER || W.isPressed())
			dy = 1;
		if (mouse.x > this.gameView.getHorizontalScreenSize() - BORDER || D.isPressed())
			dx = -1;
		if (mouse.y > this.gameView.getVerticalScreenSize() - BORDER || S.isPressed())
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		this.gameView.moveScene(dx, dy);

		double amount = input.getScroll();
		if (amount != 0)
			this.gameView.zoom((float) (amount * deltaZoom), mouse.x, mouse.y);
	}

	@Override
	public void tick(GameView scene) {
		this.tick();
	}
}
