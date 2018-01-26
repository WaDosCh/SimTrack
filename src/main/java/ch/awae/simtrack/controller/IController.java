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
 * The basic controller interface
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public interface IController {

	/**
	 * retrieves the controller-associated model
	 * 
	 * @return the model
	 */
	public IModel getModel();

	/**
	 * retrieves the controller-associated view
	 * 
	 * @return the view
	 */
	public IGameView getGameView();

	/**
	 * sets the rendering frequency
	 * 
	 * @param fps
	 *            the new frequency given as frames-per-second
	 */
	public void setFPS(int fps);

	/**
	 * sets the update frequency
	 * 
	 * @param tps
	 *            the new tick frequency given as ticks-per-second
	 */
	public void setTPS(int tps);

	/**
	 * starts the tick loop
	 */
	public void start();

	/**
	 * stops the tick loop
	 */
	public void stop();

	/**
	 * starts the rendering loop
	 */
	public void startView();

	/**
	 * stops the rendering loop
	 */
	public void stopView();

	/**
	 * retrieves the controller's mouse observer instance
	 * 
	 * @return the mouse observer
	 */
	public Mouse getMouse();

	/**
	 * retrieves the controller's keyboard observer instance
	 * 
	 * @return the keyboard observer
	 */
	public Keyboard getKeyboard();

	public PathFinding getPathfinder();

	public void setWindowTitle(String string);
	
}
