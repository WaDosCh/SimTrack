package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;

import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.game.view.GameView;
import ch.awae.simtrack.util.Resource;

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
