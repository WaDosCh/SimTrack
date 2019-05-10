package ch.awae.simtrack.scene.game.view;

import java.awt.Dimension;

import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.window.Graphics;

public abstract class ToolBarButton extends Button {

	public ToolBarButton(InputController input, Runnable action) {
		super("", input, action);
	}

	@Override
	public Dimension getPreferedDimension() {
		return new Dimension(80, 80);
	}

	@Override
	protected void renderText(Graphics g) {
		// replace usual rendering of text content by calling renderIcon with centralized graphics context
		g.push();
		g.translate(this.pos.x + this.size.width / 2, this.pos.y + this.size.height / 2);
		g.scale(0.7, 0.7);
		renderIcon(g);
		g.pop();
	}

	protected abstract void renderIcon(Graphics g);

}
