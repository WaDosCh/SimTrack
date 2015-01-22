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

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import ch.awae.simtrack.Global;

/**
 * Drawing Surface
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class Surface extends JPanel {

	private static final long serialVersionUID = -6043801963054580971L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.doPaint((Graphics2D) g);
	}

	@SuppressWarnings("static-method")
	private void doPaint(Graphics2D g) {
		if (Global.rc == null)
			return;
		Global.rc.render(g);
	}

}
