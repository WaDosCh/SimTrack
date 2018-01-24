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
package ch.awae.simtrack.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Map.Entry;

import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Renderer for track rendering
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
class TrackRenderer implements IRenderer {
	private static Color bedColour = Color.ORANGE.darker();
	private static Color bgColour = Color.GREEN.darker();

	private static Stroke arrowStroke = new BasicStroke(3);

	private final static int hexSideHalf = 1 + (int) (50 / Math.sqrt(3));
	private final static int[][] hexEdges = {
			{ 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf,
					-hexSideHalf, hexSideHalf } };
	private static Color railColour = Color.DARK_GRAY;
	private static Stroke railStroke = new BasicStroke(5);

	@Override
	public void render(Graphics2D g, IView view) {
		g.setColor(bgColour);
		IViewPort port = view.getViewPort();
		for (Entry<TileCoordinate, ITile> pair : view.getModel().getTiles()) {
			TileCoordinate pos = pair.getKey();
			ITile tile = pair.getValue();
			if (!port.isVisible(pos))
				continue;
			Graphics2D g2 = port.focusHex(pos, g);
			g2.fillPolygon(hexEdges[0], hexEdges[1], 6);
			g2.setStroke(railStroke);
			TrackRenderUtil.renderRails(g2, bedColour, railColour,
					tile.getRailPaths());
			if (tile.isFixed()) {
				g2.rotate(Math.PI / 3 * tile.getRailPaths()[0]._1.ordinal());
				g2.setColor(railColour);
				g2.setStroke(arrowStroke);
				g2.translate(30, 0);
				if (tile.isTrainDestination())
					g2.scale(-1, 1);
				g2.drawLine(0, 0, -10, -10);
				g2.drawLine(0, 0, -10, 10);
				g2.drawLine(0, -10, 10, 0);
				g2.drawLine(0, 10, 10, 0);
			}
		}
	}
}
