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

import ch.awae.simtrack.controller.ControllerFactory;
import ch.awae.simtrack.controller.IController;
import ch.awae.simtrack.gui.GUI;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.model.ModelFactory;
import ch.awae.simtrack.view.IView;
import ch.awae.simtrack.view.ViewFactory;

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

	private static void init() {
		GUI g = new GUI(1200, 800);
		IModel m = ModelFactory.getModel(25, 13, 20);
		IView v = ViewFactory.createGameView(m, g);
		IController c = ControllerFactory.buildGameController(m, v, g, 50, 50);
		c.startView();
		c.start();
	}

	public static void main(String[] args) {
		// System.setProperty("sun.java2d.trace", "log");
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		SwingUtilities.invokeLater(Main::init);
	}

}