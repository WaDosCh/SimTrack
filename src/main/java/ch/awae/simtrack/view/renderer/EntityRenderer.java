package ch.awae.simtrack.view.renderer;

import ch.awae.simtrack.model.entity.IEntity;
import ch.awae.simtrack.model.entity.Train;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;

public class EntityRenderer implements IRenderer {

	@Override
	public void render(Graphics g, IGameView view) {
		for (IEntity entity : view.getModel().getEntities()) {
			if (entity instanceof Train) {
				TrainRenderUtils.renderTrain((Train) entity, g, view);
			}
		}
	}

}
