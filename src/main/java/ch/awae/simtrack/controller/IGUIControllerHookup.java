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

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.function.Consumer;

/**
 * The GUI controller hook-up used for the observer initialisation.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public interface IGUIControllerHookup {

	/**
	 * provides a consumer that registers a given mouse adapter to the required
	 * event hooks
	 * 
	 * @return the mouse hook-up method
	 */
	public Consumer<MouseAdapter> getMouseHookup();

	/**
	 * provides a consumer that registers a given key adapter to the required
	 * event hooks
	 * 
	 * @return the keyboard hook-up method
	 */
	public Consumer<KeyAdapter> getKeyboardHookup();
	
	public Consumer<String> getWindowTitleHookup();

}
