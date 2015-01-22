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

import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * This class provides an abstract description of a hex tile. Therefore it holds
 * the tiles' coordinates.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.0.1
 */
public abstract class Tile {

	/** The position. */
	private TileCoordinate position;

	/**
	 * Instantiates a new tile.
	 *
	 * @param position
	 *            the position of this tile.
	 */
	public Tile(TileCoordinate position) {
		assert position != null;
		this.position = position;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public TileCoordinate getPosition() {
		return this.position;
	}

	/**
	 * Sets the position.
	 *
	 * @param newCoord
	 *            the new position
	 */
	public void setPosition(TileCoordinate newCoord) {
		assert newCoord != null;
		this.position = newCoord;
	}
}