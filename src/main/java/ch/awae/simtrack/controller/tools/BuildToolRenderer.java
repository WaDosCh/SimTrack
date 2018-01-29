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
package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.TrackRenderUtil;

/**
 * Renderer for the {@link BuildTool}
 * 
 * @author Andreas Wälchli
 * @version 1.5, 2015-01-26
 * @since SimTrack 0.2.2
 */
class BuildToolRenderer implements IRenderer {

	private static Stroke bullCursorStroke = new BasicStroke(6);
	private static Color darkRed = Color.RED.darker();

	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));
	private static Stroke railStroke = new BasicStroke(5);
	private BuildTool tool;

	/**
	 * creates a new renderer instance
	 * 
	 * @param tool
	 *            the tool the renderer should render
	 */
	BuildToolRenderer(BuildTool tool) {
		this.tool = tool;
	}

	@Override
	public void render(Graphics2D g, IGameView view) {
		TileCoordinate c = this.tool.getMouseTile();
		if (c == null)
			return;
		if (this.tool.isBulldozeTool()) {
			Graphics2D g2 = view.getViewPort().focusHex(c, g);
			g2.setColor(this.tool.isValid() ? Color.RED : darkRed);
			g2.setStroke(bullCursorStroke);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g2.rotate(angle);
			}
		} else {
			Graphics2D g2 = view.getViewPort().focusHex(c, g);
			g2.setStroke(railStroke);
			TrackRenderUtil.renderRails(g2,
					this.tool.isValid() ? Color.LIGHT_GRAY : Color.RED,
					this.tool.isValid() ? Color.GRAY : Color.RED,
					tool.getTrack().getRailPaths());
		}
	}
}
