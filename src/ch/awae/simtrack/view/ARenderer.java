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

import java.awt.Graphics2D;
import java.awt.Point;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * General representation of any renderer.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-17
 * @since SimTrack 0.0.1
 */
public abstract class ARenderer {

	/**
	 * The main rendering function. This will be called by the appropriate
	 * controller whenever the renderer should be active.
	 * 
	 * @param g
	 *            the graphics object to render with. This instance can be
	 *            transformed without limitations.
	 */
	public abstract void render(Graphics2D g);

	@SuppressWarnings("static-method")
	protected final Graphics2D focusHex(TileCoordinate hex, Graphics2D g) {
		SceneViewPort port = Global.port;
		Point p = port.getScreenCoordinate(port.getScenePos(hex));
		double zoomFac = port.getMaxZoom();
		zoomFac /= port.getZoom();
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(p.x, p.y);
		g2.scale(zoomFac, zoomFac);
		return g2;
	}

}
