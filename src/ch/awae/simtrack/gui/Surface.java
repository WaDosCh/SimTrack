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
import java.awt.Graphics2D;
import java.util.function.Consumer;

import javax.swing.JPanel;

/**
 * Drawing Surface
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.1
 */
class Surface extends JPanel {

	private static final long serialVersionUID = -6043801963054580971L;

	Surface(int x, int y) {
		super();
		this.setSize(x, y);
		this.setMinimumSize(new Dimension(x, y));
		this.setPreferredSize(new Dimension(x, y));
	}

	private Consumer<Graphics2D> renderer = (g) -> {
		// nop
	};

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.renderer.accept((Graphics2D) g);
	}

	void setRenderingHook(Consumer<Graphics2D> renderer) {
		this.renderer = renderer;
	}

}
