package ch.awae.simtrack.controller.tools;

import java.awt.Graphics2D;

import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;

public class InGameMenuRenderer implements IRenderer {

	@Override
	public void render(Graphics2D g, IGameView view) {
		int w2 = view.getHorizontalScreenSize() / 2;

		int h = 500;
		int y = (view.getVerticalScreenSize() - Design.toolbarHeight) / 2
				- h / 2;

		g.setColor(Design.almostTransparent);
		g.fillRect(w2 - 200, y, 400, h);
		g.setColor(Design.grayBorder);
		g.drawRect(w2 - 200, y, 400, h);
	}

}
