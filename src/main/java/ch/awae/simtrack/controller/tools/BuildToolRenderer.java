package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.renderer.IRenderer;
import ch.awae.simtrack.view.renderer.TrackRenderUtil;

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
			TrackRenderUtil.renderRails(g2, this.tool.isValid() ? Color.LIGHT_GRAY : Color.RED,
					this.tool.isValid() ? Color.GRAY : Color.RED, tool.getTrack().getRailPaths());
		}
	}
}
