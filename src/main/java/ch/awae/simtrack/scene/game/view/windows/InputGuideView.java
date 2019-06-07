package ch.awae.simtrack.scene.game.view.windows;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.window.Graphics;

public class InputGuideView extends WindowComponent {

	private Model model;

	public InputGuideView(InputController input, Model model) {
		super(Design.textFont, input);
		this.model = model;

		String[] inputGuideText = Resource.getText("inputKeys.txt");
		for (String s : inputGuideText)
			addComponent(new Label(s));
		this.title = "Input Guide";
		this.isVisible = model.getDebugOptions().getShowInputGuide().get();
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
	}

}
