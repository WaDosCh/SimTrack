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

import ch.awae.simtrack.model.BasicTrackTile;
import ch.awae.simtrack.model.TilePath;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2014-01-26
 * @since SimTrack 0.2.2
 */
class AnonymousTrack extends BasicTrackTile {

	private float cost;
	private TilePath[] connections;

	AnonymousTrack(TileCoordinate position, TilePath[] connections, float cost) {
		super(position);
		this.connections = connections.clone();
		this.cost = cost;
	}

	@Override
	public float getTravelCost() {
		return this.cost;
	}

	@Override
	public TilePath[] getRailPaths() {
		return this.connections;
	}

}
