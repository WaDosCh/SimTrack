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
import java.awt.geom.AffineTransform;

import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.tools.BuildTool;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Renderer for the {@link BuildTool}
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class BuildToolRenderer implements IRenderer {

	private static Stroke bullCursorStroke = new BasicStroke(6);
	private static Color darkRed = Color.RED.darker();

	private final static int hexSideHalf = (int) (50 / SceneViewPort.SQRT3);
	private static Stroke railStroke = new BasicStroke(5);
	private BuildTool tool;

	public BuildToolRenderer(BuildTool tool) {
		this.tool = tool;
	}

	@Override
	public void render(Graphics2D g) {
		if (this.tool.isBulldoze()) {
			TileCoordinate c = Mouse.hexPosition();
			if (c == null)
				return;
			Graphics2D g2 = IRenderer.focusHex(c, g);
			g2.setColor(this.tool.isValid() ? Color.RED : darkRed);
			g2.setStroke(bullCursorStroke);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g2.rotate(angle);
			}
		} else {
			TrackTile t = this.tool.getTrack();
			if (t.getPosition() == null)
				return;
			Graphics2D g2 = IRenderer.focusHex(t.getPosition(), g);
			g2.setColor(this.tool.isValid() ? Color.LIGHT_GRAY : Color.RED);
			AffineTransform T = g2.getTransform();
			t.renderBed(g2);
			g2.setTransform(T);
			g2.setColor(this.tool.isValid() ? Color.GRAY : Color.RED);
			g2.setStroke(railStroke);
			t.renderRail(g2);
		}
	}
}
