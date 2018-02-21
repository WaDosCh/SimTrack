package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.game.view.GameView;
import ch.awae.simtrack.scene.game.view.renderer.Renderer;
import ch.awae.simtrack.ui.BasePanel;

public class InGameMenuRenderer extends BasePanel implements Renderer {

	public InGameMenuRenderer(Input input) {
		super("Ingame Menu", input);
	}

	@Override
	public void render(Graphics graphics, GameView scene) {
		super.render(graphics, scene.getWindow());
	}

}
