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
package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.TileEdgeCoordinate;

/**
 * Basic description of a signal.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public interface ISignal {

	/**
	 * retrieves the edge of the signal.
	 * 
	 * @return the position
	 */
	public TileEdgeCoordinate getPosition();

	/**
	 * sets the position of the signal.
	 * 
	 * @param position
	 *            the new position
	 */
	public void setPosition(TileEdgeCoordinate position);

	/**
	 * indicates whether or not the signal blocks inward traffic (directions
	 * match those of a directed edge coordinate). Block occupation is
	 * irrelevant.
	 * 
	 * @return {@code true} if and only if inward traffic is not allowed.
	 */
	public boolean blocksInward();

	/**
	 * indicates whether or not the signal blocks outward traffic (directions
	 * match those of a directed edge coordinate). Block occupation is
	 * irrelevant.
	 * 
	 * @return {@code true} if and only if outward traffic is not allowed.
	 */
	public boolean blocksOutward();

}
