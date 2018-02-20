package ch.awae.simtrack.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.GameView;
import ch.awae.simtrack.view.Graphics;

public class BackgroundRenderer implements Renderer {

	private Color color = Resource.getProperties("renderer.properties").getColor("grassColor");

	@Override
	public void render(Graphics g, GameView view) {
		int x = view.getHorizontalScreenSize();
		int y = view.getVerticalScreenSize();
		g.setColor(color);
		g.fillRect(0, 0, x, y);
	}

}
