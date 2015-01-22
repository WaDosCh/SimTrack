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

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;

/**
 * Represents a border track piece. Border track pieces act as the inputs and
 * outputs for the map.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public interface BorderConnection {

	/**
	 * The connection directions.
	 * 
	 * @author Andreas Wälchli
	 * @version 1.1 (2015-01-16)
	 * @since SimTrack 0.0.1 (2015-01-16)
	 */
	public static enum Direction {
		IN, OUT;
	}

	/**
	 * retrieves the direction (input / output)
	 * 
	 * @return the direction
	 */
	public Direction getDirection();

	/**
	 * retrieves the edge that is connected to the the map.
	 * 
	 * @return the connecting edge
	 */
	public DirectedTileEdgeCoordinate getInterfacingEdge();

}
