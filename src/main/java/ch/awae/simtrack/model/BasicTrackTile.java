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
import lombok.Getter;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public abstract class BasicTrackTile implements ITile {

	private @Getter TileCoordinate tileCoordinate;

	public BasicTrackTile(TileCoordinate tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	@Override
	public boolean isFixed() {
		return false;
	}

	@Override
	public boolean isTrainSpawner() {
		return false;
	}

	@Override
	public boolean isTrainDestination() {
		return false;
	}

	@Override
	public boolean connectsAt(Edge edge) {
		TilePath[] paths = this.getRailPaths();
		for (TilePath p : paths)
			if (p._1 == edge || p._2 == edge)
				return true;
		return false;
	}

	@Override
	public void tick() {
		return;
	}

	@Override
	public void update(IModel model) {
		return;
	}

}
