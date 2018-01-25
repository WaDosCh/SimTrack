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

/**
 * General representation of any renderer.
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
@FunctionalInterface
public interface IRenderer {

	/**
	 * The main rendering function. This will be called by the appropriate
	 * controller whenever the renderer should be active.
	 * 
	 * @param g
	 *            the graphics object to render with. This instance can be
	 *            transformed without limitations.
	 * @param view
	 *            the view that invokes the rendering
	 */
	public void render(Graphics2D g, IGameView view);

}
