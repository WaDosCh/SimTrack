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
package ch.awae.simtrack.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Render For the "Free Hand" Tool
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class FreeToolRenderer extends ARenderer {

	private final FreeTool tool;
	private final static Stroke borderStroke = new BasicStroke(6);
	private final static int hexSideHalf = (int) (50 / SceneViewPort.SQRT3);

	public FreeToolRenderer(FreeTool tool) {
		this.tool = tool;
	}

	@Override
	public void render(Graphics2D g) {
		g.setStroke(borderStroke);
		Graphics2D g2 = ARenderer.focusHex(this.tool.tile, g);
		g2.setColor(Color.ORANGE);
		double angle = Math.PI / 3;
		for (int i = 0; i < 6; i++) {
			g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
			g2.rotate(angle);
		}
	}
}
