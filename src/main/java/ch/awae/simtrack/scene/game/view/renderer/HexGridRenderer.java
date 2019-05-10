package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.window.Graphics;

/**
 * Renderer for the hex grid overlay
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
		for (int i = 0; i < this.model.getTileGridSize().width; i++) {
			for (int j = 0; j < this.model.getTileGridSize().height; j++) {
				TileCoordinate hex = new TileCoordinate(i - (j / 2), j);
				if (!this.viewPort.isVisible(hex))
					continue;
				g.push();
				this.viewPort.focusHex(hex, g);
				g.drawHex();
				g.pop();
			}
		}
	}
}
