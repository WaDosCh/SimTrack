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

import java.awt.Graphics2D;

import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * This class provides an abstract definition of a track tile.
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-23
 * @since SimTrack 0.1.1 (0.0.1)
 */
public abstract class TrackTile extends Tile {

	private Block block = null;

	/**
	 * Instantiates a new track tile.
	 *
	 * @param position
	 *            the position of the tile
	 */
	public TrackTile(TileCoordinate position) {
		super(position);
	}

	public abstract TrackTile cloneTrack();

	/**
	 * @return the block
	 * @since 1.4 (SimTrack 0.1.1)
	 */
	public Block getBlock() {
		return this.block;
	}

	/**
	 * Provides a list of all path supported by the tile. This paths are all
	 * considered bidirectional.
	 * 
	 * @return a float matrix with dimensions n-by-3, that lists the n possible
	 *         paths. For each row the first two entries are the indices of the
	 *         connected edges. This must be integer values between 0 and 5
	 *         (inclusive). The third element determines the base cost of a
	 *         path.
	 */
	public abstract float[][] getRawPaths();

	public abstract void renderBed(Graphics2D g);

	public abstract void renderRail(Graphics2D g);

	/**
	 * 
	 * @param block
	 *            the new block
	 * @since 1.4 (SimTrack 0.1.1)
	 */
	public void setBlock(Block block) {
		this.block = block;
	}

	/**
	 * indicates if the tile connects at a given edge
	 * 
	 * @param edge
	 *            the edge to check for
	 * @return {@code true} if it connects
	 * @since 1.4 (SimTrack 0.1.1)
	 */
	public boolean connectsToEdge(int edge) {
		float[][] paths = this.getRawPaths();
		for (int i = 0; i < 2 * paths.length; i++)
			if (edge == (int) paths[i % paths.length][i < paths.length ? 0 : 1])
				return true;
		return false;
	}
}
