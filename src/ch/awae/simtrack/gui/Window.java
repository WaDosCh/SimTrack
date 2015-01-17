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

@SuppressWarnings("javadoc")
public class Window extends JFrame {

	private static final long serialVersionUID = 7381994043443871855L;

	private Surface surface;

	public Window(int x, int y) {
		super("SimTrack");
		this.setSize(x, y);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.surface = new Surface();
		this.add(this.surface);
		this.setResizable(false);
		this.setVisible(true);
	}

}
