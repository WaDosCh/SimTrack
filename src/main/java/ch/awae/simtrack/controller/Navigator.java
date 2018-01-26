/*
 * SimTrack - Railway Planning and Simulation Game Copyright (C) 2015 Andreas Wälchli This program
 * is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or
 * any later version. This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.view.IGameView;

/**
 * Navigation Tool. Used for Scene movement & zoom
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class Navigator {

	private Keyboard keyboard;
	private Mouse mouse;
	private IGameView gameView;

	/**
	 * instantiates a new navigator
	 */
	public Navigator(IGameView gameView, Mouse mouse, Keyboard keyboard) {
		this.gameView = gameView;
		this.mouse = mouse;
		this.keyboard = keyboard;
	}

	private final static int BORDER = 40;
	private final static float deltaZoom = -.2f;
	private boolean isActive = true;
	private final static int MOVE_SPEED = 20;

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
		Point mouse = this.mouse.getScreenPosition();
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		if (mouse.x < BORDER
				|| this.keyboard.keysOr(KeyEvent.VK_A, KeyEvent.VK_LEFT))
			dx = 1;
		if (mouse.y < BORDER
				|| this.keyboard.keysOr(KeyEvent.VK_W, KeyEvent.VK_UP))
			dy = 1;
		if (mouse.x > this.gameView.getHorizontalScreenSize() - BORDER
				|| this.keyboard.keysOr(KeyEvent.VK_D, KeyEvent.VK_RIGHT))
			dx = -1;
		if (mouse.y > this.gameView.getVerticalScreenSize() - BORDER
				|| this.keyboard.keysOr(KeyEvent.VK_S, KeyEvent.VK_DOWN))
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		this.gameView.moveScene(dx, dy);

		double amount = this.mouse.getScroll();
		this.gameView.zoom((float) (amount * deltaZoom), mouse.x, mouse.y);
	}
}
