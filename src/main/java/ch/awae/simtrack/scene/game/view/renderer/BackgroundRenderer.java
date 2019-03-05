package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.awt.Dimension;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.util.Resource;

public class BackgroundRenderer implements Renderer {

	private Color color = Resource.getConfigProperties("renderer.properties").getColor("grassColor");

	@Override
	public void render(Graphics g, Game view) {
		Dimension size = view.getScreenSize();
		g.setColor(color);
		g.fillRect(0, 0, size.width,size.height);
	}

}
