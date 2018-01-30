package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import ch.awae.simtrack.model.Train;
import ch.awae.simtrack.util.Resource;

public class TrainRenderUtils {

	public static final BufferedImage loc = Resource.getImage("locomotive1.png");

	public static void renderTrain(Train train, Graphics2D g, IGameView view) {
		AffineTransform t = g.getTransform();
		Graphics2D g2 = view.getViewPort().transformToScene(g);

		Point position = view.getViewPort().toScreenCoordinate(train.getTile());
		// g2.translate(-loc.getWidth() / 2, -loc.getHeight() / 2);
		g2.drawImage(loc, position.x, position.y, null);

		g.setTransform(t);
	}
}
