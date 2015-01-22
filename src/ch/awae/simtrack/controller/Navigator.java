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

import ch.awae.simtrack.Global;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Navigation Tool. Used for Scene movement & zoom
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Navigator implements ITool {

	@Override
	public void load(Object[] args) throws IllegalStateException {
		// no action required
	}

	@Override
	public void unload() {
		// no action required
	}

	private final static int BORDER = 20;
	private final static int MOVE_SPEED = 10;

	@Override
	public void tick() {
		Point mouse = Mouse.position();
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		if (mouse.x < BORDER
				|| Keyboard.keysOr(KeyEvent.VK_A, KeyEvent.VK_LEFT))
			dx = 1;
		if (mouse.y < BORDER || Keyboard.keysOr(KeyEvent.VK_W, KeyEvent.VK_UP))
			dy = 1;
		if (mouse.x > Global.ScreenW - BORDER
				|| Keyboard.keysOr(KeyEvent.VK_D, KeyEvent.VK_RIGHT))
			dx = -1;
		if (mouse.y > Global.ScreenH - BORDER
				|| Keyboard.keysOr(KeyEvent.VK_S, KeyEvent.VK_DOWN))
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		SceneViewPort.moveScene(dx, dy);

		double amount = Mouse.getScroll();
		SceneViewPort.zoom((int) (amount * deltaZoom), mouse.x, mouse.y);
	}

	@Override
	public String getToolName() {
		return null;
	}

	@Override
	public ARenderer getRenderer() {
		return null;
	}

	private final static int deltaZoom = 1;

}
