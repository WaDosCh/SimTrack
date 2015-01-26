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
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.2
 */
class Window extends JFrame {

	private static final long serialVersionUID = 7381994043443871855L;

	private Surface surface;

	/**
	 * instantiates a new main window
	 * 
	 * @param x
	 *            the width of the drawing surface
	 * @param y
	 *            the height of the drawing surface
	 */
	Window(int x, int y) {
		super("SimTrack");
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(this.surface = new Surface(x, y));
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setFocusTraversalKeysEnabled(false);
		this.setVisible(true);
	}

	/**
	 * retrieves the drawing surface
	 * 
	 * @return the drawing surface
	 */
	Surface getSurface() {
		return this.surface;
	}

}
