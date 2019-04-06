package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.awt.Dimension;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.util.Resource;

public class BackgroundRenderer implements Renderer {

	private Color color = Resource.getConfigProperties("renderer.properties").getColor("grassColor");
	private Window window;

	public BackgroundRenderer(Window window) {
		this.window = window;
	}
	
	@Override
	public void render(Graphics g) {
		Dimension size = window.getScreenSize();
		g.setColor(color);
		g.fillRect(0, 0, size.width,size.height);
	}

}
