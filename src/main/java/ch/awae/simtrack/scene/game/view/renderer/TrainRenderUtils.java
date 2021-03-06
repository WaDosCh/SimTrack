package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.entity.TrainElementConfiguration;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.window.Graphics;

public class TrainRenderUtils {

	static Logger logger = LogManager.getLogger();

	public static final BufferedImage loc = Resource.getImage("locomotive1.png");

	public static void renderTrain(Train train, Graphics g, ViewPortNavigator viewPort) {
		g.push();
		viewPort.transformToScene(g);
		// drawExactCircle(g, train);
		drawImage(g, train);
		g.pop();
	}

	private static void drawExactCircle(Graphics g2, Train train) {
		g2.setColor(Color.red);
		SceneCoordinate p = train.getNicePosition();
		g2.fillOval((int) p.s - 10, (int) p.t - 10, 20, 20);
	}

	private static void drawImage(Graphics g2, Train train) {
		List<TrainElementConfiguration> elements = train.getTrainElements();
		double offset = 0;
		for (TrainElementConfiguration element : elements) {
			SceneCoordinate axis1Pos = train.getPositionWithOffset(offset + element.getFirstAxle());
			SceneCoordinate axis2Pos = train.getPositionWithOffset(offset + element.getSecondAxle());
			if (axis1Pos == null || axis2Pos == null) {
				break;
			}
			g2.push();
			double angle = axis1Pos.getPointD().getAngleTo(axis2Pos.getPointD());
			g2.translate(axis2Pos.s, axis2Pos.t);
			g2.rotate(angle + Math.PI);

			BufferedImage image = element.getImage();
			g2.drawImage(element.getImage(), element.getSecondAxle() - element.getLength(), -image.getHeight() / 2,
					null);
			offset += element.getLength();
			g2.pop();
		}
	}
}
