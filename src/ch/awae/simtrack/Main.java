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
package ch.awae.simtrack;

import javax.swing.SwingUtilities;

import ch.awae.simtrack.controller.RenderingController;
import ch.awae.simtrack.gui.Window;

@SuppressWarnings("javadoc")
public class Main {

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		// System.setProperty("sun.java2d.trace", "log");

		SwingUtilities.invokeLater(Main::init);

	}

	private static void init() {
		Global.window = new Window(1200, 800);
		Global.rc = new RenderingController(Global.window, 50);
	}

}