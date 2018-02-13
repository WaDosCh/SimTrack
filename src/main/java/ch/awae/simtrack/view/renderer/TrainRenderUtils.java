package ch.awae.simtrack.view.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.model.entity.Train;
import ch.awae.simtrack.model.entity.TrainElementConfiguration;
import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.IGameView;

public class TrainRenderUtils {

	static Logger logger = LogManager.getLogger();

	public static final BufferedImage loc = Resource.getImage("locomotive1.png");

	public static void renderTrain(Train train, Graphics2D g, IGameView view) {
		Graphics2D g2 = view.getViewPort().transformToScene(g);

		drawExactCircle((Graphics2D) g2.create(), train);
	}

	private static void drawExactCircle(Graphics2D g2, Train train) {
		g2.setColor(Color.red);
		SceneCoordinate p = train.getNicePosition();
		g2.fillOval((int) p.s - 10, (int) p.t - 10, 20, 20);

		drawImage(g2, train);
	}

	private static void drawImage(Graphics2D g2, Train train) {
		List<TrainElementConfiguration> elements = train.getElements();
		double offset = 0;
		for (TrainElementConfiguration element : elements) {
			SceneCoordinate axis1Pos = train.getPositionWithOffset(offset + element.getFirstAxle());
			SceneCoordinate axis2Pos = train.getPositionWithOffset(offset + element.getSecondAxle());
			if (axis1Pos == null || axis2Pos == null) {
				break;
			}
			Graphics2D g3 = (Graphics2D) g2.create();
			double angle = axis1Pos.getPointD().getAngleTo(axis2Pos.getPointD());
			g3.translate(axis2Pos.s, axis2Pos.t);
			g3.rotate(angle + Math.PI);

			BufferedImage image = element.getImage();
			g3.drawImage(element.getImage(), element.getSecondAxle() - element.getLength(), -image.getHeight() / 2,
					null);
			offset += element.getLength();
		}
	}
}
