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
package ch.awae.simtrack.model.track;

import ch.awae.simtrack.model.RotatableTile;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Implementation for a curved rail piece.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public class CurvedRail extends TrackTile implements RotatableTile {

	/** The Constant TRAVEL_COST. */
	public static final float TRAVEL_COST = 1.2f;

	/**
	 * Instantiates a new curved rail.
	 *
	 * @param position
	 *            the position
	 */
	public CurvedRail(TileCoordinate position) {
		super(position);
		this.rotation = 0;
	}

	private int rotation = 0;

	@Override
	public void rotate(boolean isClockwise) {
		this.rotation += isClockwise ? 5 : 1;
		this.rotation %= 6;
	}

	@Override
	public void mirror() {
		// mirroring unclear since no symmetric root
	}

	@Override
	public float[][] getRawPaths() {
		return new float[][] { { this.rotation, (this.rotation + 2) % 6,
				TRAVEL_COST } };
	}

}
