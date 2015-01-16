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
import ch.awae.simtrack.properties.Layer;

/**
 * This class provides an abstract description of a hex tile. Therefore it holds
 * the tiles' coordinates and its layer. This layering can be used to avoid
 * re-rendering unchanged content. For the layer definition see {@link Layer}.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public abstract class Tile {

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

	/** The position. */
	private TileCoordinate position;

	/** The layer. */
	private int layer = -1;

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

	/**
	 * Gets the layer of this tile.
	 *
	 * @return the layer of this tile is located on
	 */
	public int getLayer() {
		return this.layer;
	}

	/**
	 * Sets the layer of this tile. This does not enforce re-rendering of the
	 * previous or the new layer. The re-rendering has to be initiated
	 * externally.
	 *
	 * @param newLayer
	 *            the new layer this tile should be located on.
	 */
	public void setLayer(int newLayer) {
		this.layer = newLayer;
	}
}