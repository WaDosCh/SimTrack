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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;

public class Surface extends JPanel {

	public Surface() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.doPaint((Graphics2D) g);
	}

	@SuppressWarnings("static-method")
	private void doPaint(Graphics2D g) {
		/*
		 * g.setColor(Color.BLACK); for (int i = 0; i < 20; i++) { for (int j =
		 * 0; j < 20; j++) { Graphics2D g2 = ARenderer.focusHex(new
		 * TileCoordinate(i, j), g); for (int k = 0; k < 3; k++) {
		 * g2.drawLine(25, -14, 25, 14); g2.rotate(Math.PI / 3); } } }
		 */
		if (Global.rc == null)
			return;
		Global.rc.render(g);
	}

}
