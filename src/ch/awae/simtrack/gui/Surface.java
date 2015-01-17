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

public class Surface extends JPanel {

	public Surface() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.doPaint((Graphics2D) g);
	}

	private void doPaint(Graphics2D g) {
		g.setBackground(Color.BLACK);
		g.setColor(Color.LIGHT_GRAY);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(50, 50);
		g2.fillRect(20, 20, 100, 40);
		g2.setStroke(new BasicStroke(5));
		g.setColor(Color.GRAY);
		g.draw3DRect(20, 20, 100, 40, false);
	}

}
