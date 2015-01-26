/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

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
	private final static int railGauge = 35;

	private final static int halfSide = (int) (50 / Math.sqrt(3));

	private static void renderCurvedRail(Graphics2D g) {
		for (int i = 0; i < 2; i++) {
			int radius = 3 * halfSide
					+ (i == 0 ? railGauge / 2 : -railGauge / 2);
			g.drawArc(50 - radius - 2, -(3 * halfSide + radius),
					2 * radius + 3, 2 * radius, 210, 60);
		}
	}

	private static void renderCurvedRailbed(Graphics2D g) {
		AffineTransform transform = g.getTransform();
		int radius = 3 * halfSide;
		double step = Math.PI / (3 * sleeperCount);
		g.rotate(-step / 2, 50, -radius);
		for (int i = 0; i < sleeperCount; i++) {
			g.rotate(step, 50, -radius);
			g.fillRect(50 - sleeperWidth / 2, -sleeperHeight / 2, sleeperWidth,
					sleeperHeight);
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
			g.fillRect(-sleeperWidth / 2, -sleeperHeight / 2, sleeperWidth,
					sleeperHeight);
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
	public static void renderRails(Graphics2D g, Color sleepers, Color rails,
			int[] network) {
		AffineTransform Tx = g.getTransform();
		for (int pass = 0; pass < 2; pass++) {
			g.setColor(pass == 0 ? sleepers : rails);
			for (int i = 0; i + 1 < network.length; i += 2) {
				g.rotate(-Math.PI / 3 * network[i]);
				switch ((network[i + 1] - network[i] + 6) % 6) {
				case 2:
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
				case 4:
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
