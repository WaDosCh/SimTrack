package ch.awae.simtrack.scene.game.view.renderer;

import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Entity;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.window.Graphics;

public class EntityRenderer implements Renderer {

	private Model model;
	private ViewPortNavigator viewPort;

	public EntityRenderer(ViewPortNavigator viewPort, Model model) {
		this.model = model;
		this.viewPort = viewPort;
	}

	@Override
	public void render(Graphics g) {
		for (Entity entity : this.model.getEntities()) {
			if (entity instanceof Train) {
				TrainRenderUtils.renderTrain((Train) entity, g, this.viewPort);
			}
		}
	}

}
