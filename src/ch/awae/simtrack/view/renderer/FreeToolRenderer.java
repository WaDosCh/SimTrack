package ch.awae.simtrack.view.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

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
