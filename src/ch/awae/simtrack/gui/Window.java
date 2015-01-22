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
package ch.awae.simtrack.gui;

import javax.swing.JFrame;

/**
 * Main GUI Window
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 */
public class Window extends JFrame {

	public static Window INSTANCE = null;
	private static final long serialVersionUID = 7381994043443871855L;

	public static void init(int x, int y) {
		INSTANCE = new Window(x, y);
	}

	public static Window instance() {
		return INSTANCE;
	}

	public Window(int x, int y) {
		super("SimTrack");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Surface.init(x, y);
		this.add(Surface.INSTANCE);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setFocusTraversalKeysEnabled(false);
		this.setVisible(true);
	}

}
