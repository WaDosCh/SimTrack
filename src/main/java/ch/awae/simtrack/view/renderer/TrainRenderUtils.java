package ch.awae.simtrack.view.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ch.awae.simtrack.model.entity.Train;
import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.IGameView;

public class TrainRenderUtils {

	public static final BufferedImage loc = Resource.getImage("locomotive1.png");

	public static void renderTrain(Train train, Graphics2D g, IGameView view) {
		Graphics2D g2 = view.getViewPort().transformToScene(g);

		TileEdgeCoordinate position = train.getPosition();
		SceneCoordinate scenePosition = view.getViewPort().toSceneCoordinate(position.tile);
		double angle = position.edge.getAngle();

		g2.translate((int) scenePosition.s, (int) scenePosition.t);
		g2.rotate(angle);
		g2.drawImage(loc, -loc.getWidth() / 2, -loc.getHeight() / 2, null);

	}
}
