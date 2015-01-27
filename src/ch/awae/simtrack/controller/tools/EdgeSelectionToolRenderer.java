package ch.awae.simtrack.controller.tools;

import java.awt.Color;
import java.awt.Graphics2D;

import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IView;

class EdgeSelectionToolRenderer implements IRenderer {

	private EdgeSelectionTool tool;

	private final static int radius = 25 ;//(int) (100 / Math.sqrt(27));

	public EdgeSelectionToolRenderer(EdgeSelectionTool tool) {
		this.tool = tool;
	}

	@Override
	public void render(Graphics2D g, IView view) {
		Graphics2D g2 = view.getViewPort().focusHex(this.tool.tc, g);
		g2.setColor(Color.YELLOW);
		for (int i = 0; i < 6; i++) {
			g2.fillOval(50 - radius, -radius, 2 * radius, 2 * radius);
			g2.rotate(Math.PI / 3);
		}
	}

}
