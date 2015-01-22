package ch.awae.simtrack.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.BorderConnection.Direction;
import ch.awae.simtrack.model.track.BorderTrackTile;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

public class BorderConnectionRenderer extends ARenderer {

	private final static int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
	private final static int[][] hexEdges = {
			{ 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf,
					-hexSideHalf, hexSideHalf } };

	@Override
	public void render(Graphics2D g) {
		Global.map.getBorderTracks().values().forEach(t -> draw(t, g));
	}

	private static void draw(BorderTrackTile t, Graphics2D g) {
		Graphics2D g2 = ARenderer.focusHex(t.getPosition(), g);
		g2.setColor(Color.GREEN.darker());
		g2.fillPolygon(hexEdges[0], hexEdges[1], 6);
		g2.rotate(-t.getEdge() * Math.PI / 3);
		g2.setColor(Color.orange.darker());
		TrackRenderUtil.renderStraightRailbed(g2, 10, 5, 45);
		// TrackRenderUtil.renderCurvedRailbed(g2, 8, 5, 40);
		g2.setColor(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(5));
		TrackRenderUtil.renderStraightRail(g2, 30);
		// TrackRenderUtil.renderCurvedRail(g2, 30);
		g2.setStroke(new BasicStroke(3));
		g2.translate(30, 0);
		if (t.getDirection() == Direction.OUT)
			g2.scale(-1, 1);
		g2.drawLine(0, 0, -10, -10);
		g2.drawLine(0, 0, -10, 10);
		g2.drawLine(0, -10, 10, 0);
		g2.drawLine(0, 10, 10, 0);
	}
}
