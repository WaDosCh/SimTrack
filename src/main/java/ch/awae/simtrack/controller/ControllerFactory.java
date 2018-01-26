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

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;

/**
 * Factory for controller instances.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class ControllerFactory {

	/**
	 * creates a new game controller. A game controller manages the in-game
	 * processes.
	 * 
	 * @param model
	 * @param view
	 * @param hooker
	 * @param tps
	 * @param fps
	 * @return a new game controller instantiated with the provided params
	 */
	public static IController buildGameController(IModel model, IGameView view,
			IGUIControllerHookup hooker, int tps, int fps) {
		Mouse mouse = new Mouse(view, hooker);
		Keyboard keyboard = new Keyboard(hooker);
		GameController c = new GameController(model, view, mouse, keyboard);
		c.setTPS(tps);
		c.setFPS(fps);
		return c;
	}

}
