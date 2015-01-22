package ch.awae.simtrack.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.controller.tools.BuildTool;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

public class BuildToolRenderer extends ARenderer {

	private BuildTool tool;
	private final static int hexSideHalf = (int) (50 / SceneViewPort.SQRT3);

	public BuildToolRenderer(BuildTool tool) {
		this.tool = tool;
	}

	@Override
	public void render(Graphics2D g) {
		if (this.tool.isBulldoze()) {
			TileCoordinate c = Global.mouse.hexPosition();
			if (c == null)
				return;
			Graphics2D g2 = ARenderer.focusHex(c, g);
			g2.setColor(this.tool.isValid() ? Color.RED : Color.RED.darker());
			g2.setStroke(new BasicStroke(6));
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g2.rotate(angle);
			}
		} else {
			TrackTile t = this.tool.getTrack();
			if (t.getPosition() == null)
				return;
			Graphics2D g2 = ARenderer.focusHex(t.getPosition(), g);
			g2.setColor(this.tool.isValid() ? Color.LIGHT_GRAY : Color.RED);
			AffineTransform T = g2.getTransform();
			t.renderBed(g2);
			g2.setTransform(T);
			g2.setColor(this.tool.isValid() ? Color.GRAY : Color.RED);
			g2.setStroke(new BasicStroke(5));
			t.renderRail(g2);
		}
	}
}
