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

import ch.awae.simtrack.HighLogic;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Renderer for track rendering
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class BaseTrackRenderer implements IRenderer {
	private static Color bedColour = Color.ORANGE.darker();
	private static Color bgColour = Color.GREEN.darker();

	private final static int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
	private final static int[][] hexEdges = {
			{ 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf,
					-hexSideHalf, hexSideHalf } };
	private static Color railColour = Color.DARK_GRAY;
	private static Stroke railStroke = new BasicStroke(5);

	private static void renderTrackTile(Graphics2D g, TileCoordinate pos,
			TrackTile tile) {
		Graphics2D g2 = IRenderer.focusHex(pos, g);
		g2.setColor(bgColour);
		g2.fillPolygon(hexEdges[0], hexEdges[1], 6);
		g2.setColor(bedColour);
		AffineTransform T = g2.getTransform();
		tile.renderBed(g2);
		g2.setTransform(T);
		g2.setColor(railColour);
		g2.setStroke(railStroke);
		tile.renderRail(g2);
	}

	@Override
	public void render(Graphics2D g) {
		HighLogic.map.getTrackPieces().forEach(
				(p, t) -> renderTrackTile(g, p, t));
	}
}
