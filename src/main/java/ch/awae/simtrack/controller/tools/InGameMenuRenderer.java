package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.gui.layout.BasePanel;
import ch.awae.simtrack.view.GameView;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.renderer.Renderer;

public class InGameMenuRenderer extends BasePanel implements Renderer {

	public InGameMenuRenderer(Input input) {
		super("Ingame Menu", input);
	}

	@Override
	public void render(Graphics graphics, GameView scene) {
		super.render(graphics, scene.getWindow());
	}

}
