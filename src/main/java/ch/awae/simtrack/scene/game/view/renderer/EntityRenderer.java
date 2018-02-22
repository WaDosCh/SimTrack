package ch.awae.simtrack.scene.game.view.renderer;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.entity.Entity;
import ch.awae.simtrack.scene.game.model.entity.Train;

public class EntityRenderer implements Renderer {

	@Override
	public void render(Graphics g, Game view) {
		for (Entity entity : view.getModel().getEntities()) {
			if (entity instanceof Train) {
				TrainRenderUtils.renderTrain((Train) entity, g, view);
			}
		}
	}

}
