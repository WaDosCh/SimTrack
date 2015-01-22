/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.view.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import ch.awae.simtrack.HighLogic;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Renderer for the hex grid overlay
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class HexGridRenderer extends ARenderer {

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		int hexSideHalf = 1 + (int) (50 / SceneViewPort.SQRT3);
		for (int i = 0; i < HighLogic.map.getHorizontalSize(); i++) {
			for (int j = 0; j < HighLogic.map.getVerticalSize(); j++) {
				int l = i - (j / 2);
				Graphics2D g2 = ARenderer.focusHex(new TileCoordinate(l, j), g);
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
