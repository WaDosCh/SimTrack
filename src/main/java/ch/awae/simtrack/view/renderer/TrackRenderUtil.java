package ch.awae.simtrack.view.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import ch.awae.simtrack.model.position.TilePath;

/**
 * Rendering utilities for the rail rendering
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class TrackRenderUtil {

	private final static int sleeperCount = 8;
	private final static int sleeperWidth = 5;
	private final static int sleeperHeight = 45;
	private final static int railGauge = 28;

	private final static int halfSide = (int) (50 / Math.sqrt(3));

	private static void renderCurvedRail(Graphics2D g) {
		for (int i = 0; i < 2; i++) {
			int radius = 3 * halfSide + (i == 0 ? railGauge / 2 : -railGauge / 2);
			g.drawArc(50 - radius - 2, -(3 * halfSide + radius), 2 * radius + 3, 2 * radius, 210, 60);
		}
	}

	private static void renderCurvedRailbed(Graphics2D g) {
		AffineTransform transform = g.getTransform();
		int radius = 3 * halfSide;
		double step = Math.PI / (3 * sleeperCount);
		g.rotate(-step / 2, 50, -radius);
		for (int i = 0; i < sleeperCount; i++) {
			g.rotate(step, 50, -radius);
			g.fillRect(50 - sleeperWidth / 2, -sleeperHeight / 2, sleeperWidth, sleeperHeight);
		}
		g.setTransform(transform);
	}

	private static void renderStraightRail(Graphics2D g) {
		g.drawLine(-50, railGauge / 2, 50, railGauge / 2);
		g.drawLine(-50, -railGauge / 2, 50, -railGauge / 2);
	}

	private static void renderStraightRailbed(Graphics2D g) {
		AffineTransform transform = g.getTransform();
		int step = 100 / sleeperCount;
		g.translate(-50 - step / 2, 0);
		for (int i = 0; i < sleeperCount; i++) {
			g.translate(step, 0);
			g.fillRect(-sleeperWidth / 2, -sleeperHeight / 2, sleeperWidth, sleeperHeight);
		}
		g.setTransform(transform);
	}

	/**
	 * renders a given rail connection network
	 * 
	 * @param g
	 * @param sleepers
	 * @param rails
	 * @param network
	 */
	public static void renderRails(Graphics2D g, Color sleepers, Color rails, TilePath[] network) {
		AffineTransform Tx = g.getTransform();
		for (int pass = 0; pass < 2; pass++) {
			g.setColor(pass == 0 ? sleepers : rails);
			for (int i = 0; i < network.length; i++) {
				g.rotate(Math.PI / 3 * network[i]._1.ordinal());
				switch ((network[i]._2.ordinal() - network[i]._1.ordinal() + 6) % 6) {
					case 4:
						if (pass == 0)
							TrackRenderUtil.renderCurvedRailbed(g);
						else
							TrackRenderUtil.renderCurvedRail(g);
						break;
					case 3:
						if (pass == 0)
							TrackRenderUtil.renderStraightRailbed(g);
						else
							TrackRenderUtil.renderStraightRail(g);
						break;
					case 2:
						g.scale(1, -1);
						if (pass == 0)
							TrackRenderUtil.renderCurvedRailbed(g);
						else
							TrackRenderUtil.renderCurvedRail(g);
						break;
					default:
						break;
				}
				g.setTransform(Tx);
			}
		}
	}
}
