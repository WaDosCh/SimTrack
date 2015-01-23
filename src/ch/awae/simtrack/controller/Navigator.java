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
package ch.awae.simtrack.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;

/**
 * Navigation Tool. Used for Scene movement & zoom
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class Navigator {

	IController owner;

	Navigator(IController c) {
		this.owner = c;
	}

	private final static int BORDER = 20;
	private final static float deltaZoom = 0.1f;

	private boolean isActive = true;

	private final static int MOVE_SPEED = 10;

	public boolean isEnabled() {
		return this.isActive;
	}

	public void setEnabled(boolean isEnabled) {
		this.isActive = isEnabled;
	}

	public void tick() {
		Point mouse = this.owner.getMouse().position();
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		if (mouse.x < BORDER
				|| this.owner.getKeyboard().keysOr(KeyEvent.VK_A,
						KeyEvent.VK_LEFT))
			dx = 1;
		if (mouse.y < BORDER
				|| this.owner.getKeyboard().keysOr(KeyEvent.VK_W,
						KeyEvent.VK_UP))
			dy = 1;
		if (mouse.x > this.owner.getView().getHorizontalScreenSize() - BORDER
				|| this.owner.getKeyboard().keysOr(KeyEvent.VK_D,
						KeyEvent.VK_RIGHT))
			dx = -1;
		if (mouse.y > this.owner.getView().getVerticalScreenSize() - BORDER
				|| this.owner.getKeyboard().keysOr(KeyEvent.VK_S,
						KeyEvent.VK_DOWN))
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		this.owner.getView().moveScene(dx, dy);

		double amount = this.owner.getMouse().getScroll();
		this.owner.getView().zoom((float) (amount * deltaZoom), mouse.x,
				mouse.y);
	}
}
