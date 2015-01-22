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

import ch.awae.simtrack.HighLogic;
import ch.awae.simtrack.model.BorderConnection.Direction;
import ch.awae.simtrack.model.track.BorderTrackTile;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Renderer for the "border tracks"
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class BorderConnectionRenderer extends ARenderer {

	private final static int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
	private final static int[][] hexEdges = {
			{ 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf,
					-hexSideHalf, hexSideHalf } };

	private static Color bgColour = Color.GREEN.darker();
	private static Color bedColour = Color.ORANGE.darker();
	private static Color railColour = Color.DARK_GRAY;
	private static Stroke railStroke = new BasicStroke(5);
	private static Stroke arrowStroke = new BasicStroke(3);

	@Override
	public void render(Graphics2D g) {
		HighLogic.map.getBorderTracks().values().forEach(t -> draw(t, g));
	}

	private static void draw(BorderTrackTile t, Graphics2D g) {
		Graphics2D g2 = ARenderer.focusHex(t.getPosition(), g);
		g2.setColor(bgColour);
		g2.fillPolygon(hexEdges[0], hexEdges[1], 6);
		g2.rotate(-t.getEdge() * Math.PI / 3);
		g2.setColor(bedColour);
		TrackRenderUtil.renderStraightRailbed(g2, 10, 5, 45);
		g2.setColor(railColour);
		g2.setStroke(railStroke);
		TrackRenderUtil.renderStraightRail(g2, 30);
		g2.setStroke(arrowStroke);
		g2.translate(30, 0);
		if (t.getDirection() == Direction.OUT)
			g2.scale(-1, 1);
		g2.drawLine(0, 0, -10, -10);
		g2.drawLine(0, 0, -10, 10);
		g2.drawLine(0, -10, 10, 0);
		g2.drawLine(0, 10, 10, 0);
	}
}
