package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.awt.Dimension;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.util.Resource;

public class BackgroundRenderer implements Renderer {

	private Color color = Resource.getConfigProperties("renderer.properties").getColor("grassColor");

	public BackgroundRenderer() {
	}
	
	@Override
	public void render(Graphics g) {
		Dimension size = g.getClipBounds().getSize();
		g.setColor(color);
		g.fillRect(0, 0, size.width,size.height);
	}

}
