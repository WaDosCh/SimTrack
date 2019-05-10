package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.awt.Dimension;

import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.window.Graphics;

public class BackgroundRenderer implements Renderer {

	private Color GRASS_COLOR = Resource.getConfigProperties("renderer.properties").getColor("grassColor");

	public BackgroundRenderer() {
	}
	
	@Override
	public void render(Graphics g) {
		Dimension size = g.getClipBounds().getSize();
		g.setColor(GRASS_COLOR);
		g.fillRect(0, 0, size.width,size.height);
	}

}
