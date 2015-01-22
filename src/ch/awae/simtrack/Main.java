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

import ch.awae.simtrack.controller.BasicBorderConnectionSpawner;
import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.RenderingController;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Main Class.
 * 
 * Will be replaced by a more elaborate high-logic system.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Main {

	public static void main(String[] args) {
		// System.setProperty("sun.java2d.trace", "log");
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		SwingUtilities.invokeLater(Main::init);
	}

	private static void init() {
		Window.init(1200, 800);
		HighLogic.map = new Map(26, 15, new BasicBorderConnectionSpawner(20));
		SceneViewPort.init(HighLogic.map, Window.instance());
		Mouse.init();
		Keyboard.init();
		Editor.init(50);
		RenderingController.init(100);
		Editor.start();
		RenderingController.start();
	}

}