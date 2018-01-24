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

import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Implementation for the border track pieces. They do not contain any paths.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
class BorderTrackTile extends BasicTrackTile {

	private Edge edge;
	private boolean isOutput;

	public BorderTrackTile(TileCoordinate position, Edge edge, boolean isOutput) {
		super(position);
		this.edge = edge;
		this.isOutput = isOutput;
	}

	@Override
	public float getTravelCost() {
		return 1;
	}

	@Override
	public TilePath[] getRailPaths() {
		return new TilePath[] { new TilePath(edge, edge.getOpposite()) };
	}

	@Override
	public boolean isFixed() {
		return true;
	}

	@Override
	public boolean isTrainDestination() {
		return this.isOutput;
	}

	@Override
	public boolean isTrainSpawner() {
		return !this.isOutput;
	}

	@Override
	public boolean connectsAt(Edge edge) {
		return edge == this.edge;
	}

}