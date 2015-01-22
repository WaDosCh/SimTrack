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
import java.awt.geom.AffineTransform;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Renderer for track rendering
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class BaseTrackRenderer extends ARenderer {
	private final static int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
	private final static int[][] hexEdges = {
			{ 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf,
					-hexSideHalf, hexSideHalf } };

	@Override
	public void render(Graphics2D g) {
		Global.map.getTrackPieces().forEach((p, t) -> renderTrackTile(g, p, t));
	}

	private static void renderTrackTile(Graphics2D g, TileCoordinate pos,
			TrackTile tile) {
		Graphics2D g2 = ARenderer.focusHex(pos, g);
		g2.setColor(Color.GREEN.darker());
		g2.fillPolygon(hexEdges[0], hexEdges[1], 6);
		g2.setColor(Color.ORANGE.darker());
		AffineTransform T = g2.getTransform();
		tile.renderBed(g2);
		g2.setTransform(T);
		g2.setColor(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(5));
		tile.renderRail(g2);
	}
}
