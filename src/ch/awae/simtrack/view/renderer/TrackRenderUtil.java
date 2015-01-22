package ch.awae.simtrack.view.renderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import ch.awae.simtrack.view.SceneViewPort;

public class TrackRenderUtil {

	private final static int halfSide = (int) (50 / SceneViewPort.SQRT3);
	private final static int curveCen = (50 - halfSide) / 2;

	public static void renderStraightRailbed(Graphics2D g, int count,
			int width, int height) {
		AffineTransform transform = g.getTransform();
		int step = 100 / count;
		g.translate(-50 - step / 2, 0);
		for (int i = 0; i < count; i++) {
			g.translate(step, 0);
			g.fillRect(-width / 2, -height / 2, width, height);
		}
		g.setTransform(transform);
	}

	public static void renderCurvedRailbed(Graphics2D g, int count, int width,
			int height) {
		AffineTransform transform = g.getTransform();
		int radius = 3 * halfSide;
		double step = Math.PI / (3 * count);
		g.rotate(-step / 2, 50, -radius);
		for (int i = 0; i < count; i++) {
			g.rotate(step, 50, -radius);
			g.fillRect(50 - width / 2, -height / 2, width, height);
		}
		g.setTransform(transform);
	}

	public static void renderCurvedRail(Graphics2D g, int gap) {
		for (int i = 0; i < 2; i++) {
			int radius = 3 * halfSide + (i == 0 ? gap / 2 : -gap / 2);
			g.drawArc(50 - radius - 2, -(3 * halfSide + radius),
					2 * radius + 3, 2 * radius, 210, 60);
		}
	}

	public static void renderStraightRail(Graphics2D g, int gap) {
		g.drawLine(-50, gap / 2, 50, gap / 2);
		g.drawLine(-50, -gap / 2, 50, -gap / 2);
	}
}
