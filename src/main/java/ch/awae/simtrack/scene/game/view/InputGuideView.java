package ch.awae.simtrack.scene.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.window.Graphics;

public class InputGuideView extends WindowComponent {

	private String[] inputGuideText;
	private Model model;

	public InputGuideView(InputController input, Model model) {
		super(Design.textFont, input);
		this.model = model;

		this.inputGuideText = Resource.getText("inputKeys.txt");
		this.title = "Input Guide";
		this.isVisible = model.getDebugOptions().getShowInputGuide().get();
	}

	@Override
	public Dimension getPreferedDimension() {
		return new Dimension(300, 350);
	}
	
	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.INPUT_GUIDE)) {
			AtomicBoolean visible = this.model.getDebugOptions().getShowInputGuide();
			visible.set(!visible.get());
			this.isVisible = visible.get();
		}
		super.handleInput(event);
	}

	@Override
	public void render(Graphics g) {
		if (!this.isVisible)
			return;
		super.render(g);
		renderUserGuide(g);
	}

	private void renderUserGuide(Graphics g) {
		g.push();
		g.translate(this.pos.x, this.pos.y);
		int y = 80;
		g.setColor(Color.black);
		g.setFont(Design.textFont);
		for (String s : this.inputGuideText) {
			g.drawString(s, 20, y);
			y += Design.textFont.getSize();
		}
		g.pop();
	}

}
