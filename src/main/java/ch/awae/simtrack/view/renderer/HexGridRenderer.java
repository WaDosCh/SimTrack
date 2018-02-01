package ch.awae.simtrack.view.renderer;

import java.awt.Color;
import java.awt.Graphics2D;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IViewPort;

/**
 * Renderer for the hex grid overlay
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class HexGridRenderer implements IRenderer {

	@Override
	public void render(Graphics2D g, IGameView view) {
		IViewPort port = view.getViewPort();
		g.setColor(Color.BLACK);
		int hexSideHalf = 1 + (int) (50 / Math.sqrt(3));
		for (int i = 0; i < view.getModel().getHorizontalSize(); i++) {
			for (int j = 0; j < view.getModel().getVerticalSize(); j++) {
				TileCoordinate hex = new TileCoordinate(i - (j / 2), j);
				if (!port.isVisible(hex))
					continue;
				Graphics2D g2 = port.focusHex(hex, g);
				for (int k = 0; k < 3; k++) {
					g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
					g2.rotate(Math.PI / 3);
				}
				if (j == 0) {
					for (int a = 0; a < 2; a++) {
						g2.rotate(Math.PI / 3);
						g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
					}
				}

			}
		}
	}
}
