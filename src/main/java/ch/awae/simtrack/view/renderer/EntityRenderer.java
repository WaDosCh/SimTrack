package ch.awae.simtrack.view.renderer;

import ch.awae.simtrack.model.entity.Entity;
import ch.awae.simtrack.model.entity.Train;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;

public class EntityRenderer implements Renderer {

	@Override
	public void render(Graphics g, IGameView view) {
		for (Entity entity : view.getModel().getEntities()) {
			if (entity instanceof Train) {
				TrainRenderUtils.renderTrain((Train) entity, g, view);
			}
		}
	}

}
