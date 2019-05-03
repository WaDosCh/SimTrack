package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.util.Resource;

/**
 * Renderer for the hex grid overlay
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class HexGridRenderer implements Renderer {

	private final static Color gridColor = Resource.getConfigProperties("renderer.properties").getColor("gridColor");
	private Model model;
	private ViewPortNavigator viewPort;

	public HexGridRenderer(ViewPortNavigator viewPort, Model model) {
		this.viewPort = viewPort;
		this.model = model;
	}
	
	
	@Override
	public void render(Graphics g) {
		if (!this.model.getDrawGrid().get())
			return;
		g.setColor(gridColor);
		int hexSideHalf = 1 + (int) (50 / Math.sqrt(3));
		for (int i = 0; i < this.model.getTileGridSize().width; i++) {
			for (int j = 0; j < this.model.getTileGridSize().height; j++) {
				TileCoordinate hex = new TileCoordinate(i - (j / 2), j);
				if (!this.viewPort.isVisible(hex))
					continue;
				g.push();
				this.viewPort.focusHex(hex, g);
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
