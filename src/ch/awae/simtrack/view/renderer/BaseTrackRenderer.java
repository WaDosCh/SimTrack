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

public class BaseTrackRenderer extends ARenderer {
	private final static int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
	private final static int[][] hexEdges = {
			{ 0, -50, -50, 0, 50, 50 },
			{ 2 * hexSideHalf, hexSideHalf, -hexSideHalf, -2 * hexSideHalf,
					-hexSideHalf, hexSideHalf } };

	@Override
	public void render(Graphics2D g) {
		Global.map.getTrackPieces().forEach(
				(p, t) -> this.renderTrackTile(g, p, t));
	}

	private void renderTrackTile(Graphics2D g, TileCoordinate pos,
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
