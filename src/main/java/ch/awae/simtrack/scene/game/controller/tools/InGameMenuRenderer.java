package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.view.renderer.Renderer;

public class InGameMenuRenderer extends BasePanel implements Renderer {

	public InGameMenuRenderer(Input input) {
		super("Ingame Menu", input, true);
	}

	@Override
	public void render(Graphics graphics, Game scene) {
		super.render(graphics, scene.getWindow());
	}

}
