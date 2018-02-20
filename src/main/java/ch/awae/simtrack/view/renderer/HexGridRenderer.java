package ch.awae.simtrack.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.ViewPort;

/**
 * Renderer for the hex grid overlay
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class HexGridRenderer implements IRenderer {

	private final static Color gridColor = Resource.getProperties("renderer.properties").getColor("gridColor");

	@Override
	public void render(Graphics g, IGameView view) {
		ViewPort port = view.getViewPort();
		g.setColor(gridColor);
		int hexSideHalf = 1 + (int) (50 / Math.sqrt(3));
		for (int i = 0; i < view.getModel().getHorizontalSize(); i++) {
			for (int j = 0; j < view.getModel().getVerticalSize(); j++) {
				TileCoordinate hex = new TileCoordinate(i - (j / 2), j);
				if (!port.isVisible(hex))
					continue;
				g.push();
				port.focusHex(hex, g);
				for (int k = 0; k < 3; k++) {
					g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
					g.rotate(Math.PI / 3);
				}
				if (j == 0) {
					for (int a = 0; a < 2; a++) {
						g.rotate(Math.PI / 3);
						g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
					}
				}
				g.pop();
			}
		}
	}
}
