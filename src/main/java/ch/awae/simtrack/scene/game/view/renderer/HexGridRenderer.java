package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.view.ViewPort;
import ch.awae.simtrack.util.Resource;

/**
 * Renderer for the hex grid overlay
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class HexGridRenderer implements Renderer {

	private final static Color gridColor = Resource.getProperties("renderer.properties").getColor("gridColor");

	@Override
	public void render(Graphics g, Game view) {
		if (!view.drawHex)
			return;
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
