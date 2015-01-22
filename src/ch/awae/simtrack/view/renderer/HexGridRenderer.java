package ch.awae.simtrack.view.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

public class HexGridRenderer extends ARenderer {

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
		for (int i = 0; i < Global.map.getHorizontalSize(); i++) {
			for (int j = 0; j < Global.map.getVerticalSize(); j++) {
				int l = i - (j / 2);
				Graphics2D g2 = ARenderer.focusHex(new TileCoordinate(l, j), g);
				// g2.drawString(l + "|" + j, -20, 0);
				/**
				 * if (i == 0) { g2.scale(2, 2); g2.drawLine(-12, -7, -12, 7);
				 * g2.scale(0.5, 0.5); } else if (i ==
				 * (Global.map.getHorizontalSize() - 1)) { g2.scale(2, 2);
				 * g2.drawLine(5, -7, 5, 7); g2.scale(0.5, 0.5); }
				 **/
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
