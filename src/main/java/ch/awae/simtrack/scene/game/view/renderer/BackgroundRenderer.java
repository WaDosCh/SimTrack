package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.util.Resource;

public class BackgroundRenderer implements Renderer {

	private Color color = Resource.getProperties("renderer.properties").getColor("grassColor");

	@Override
	public void render(Graphics g, Game view) {
		int x = view.getHorizontalScreenSize();
		int y = view.getVerticalScreenSize();
		g.setColor(color);
		g.fillRect(0, 0, x, y);
	}

}
