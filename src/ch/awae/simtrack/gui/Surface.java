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

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import ch.awae.simtrack.controller.RenderingController;

/**
 * Drawing Surface
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Surface extends JPanel {

	public static Surface INSTANCE = null;

	private static final long serialVersionUID = -6043801963054580971L;

	public static void init(int x, int y) {
		INSTANCE = new Surface(x, y);
	}

	public static Surface instance() {
		return INSTANCE;
	}

	public Surface(int x, int y) {
		super();
		this.setSize(x, y);
		this.setMinimumSize(new Dimension(x, y));
		this.setPreferredSize(new Dimension(x, y));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		RenderingController.render(g);
	}

}
