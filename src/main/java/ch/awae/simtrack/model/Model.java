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

import java.util.*;
import java.util.Map.Entry;

import ch.awae.simtrack.model.Signal.Type;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.util.T3;

class Model implements IModel {

	private int sizeX, sizeY;

	Model(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	private HashMap<TileCoordinate, ITile> tiles = new HashMap<>();
	private HashMap<TileEdgeCoordinate, Signal> signals = new HashMap<>();

	@Override
	public int getHorizontalSize() {
		return this.sizeX;
	}

	@Override
	public int getVerticalSize() {
		return this.sizeY;
	}

	@Override
	public ITile getTileAt(TileCoordinate position) {
		return this.tiles.get(position);
	}

	@Override
	public void setTileAt(TileCoordinate position, ITile tile) {
		if (this.tiles.containsKey(position))
			return;
		this.tiles.put(position, tile);
	}

	@Override
	public Set<Map.Entry<TileCoordinate, ITile>> getTiles() {
		return tiles.entrySet();
	}

	@Override
	public void removeTileAt(TileCoordinate position) throws IllegalArgumentException {
		ITile tile = this.tiles.get(position);
		if (tile == null || tile instanceof IFixedTile)
			throw new IllegalArgumentException();
		this.tiles.remove(position);
	}

	@Override
	public List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> getPaths(TileCoordinate position) {
		ITile tile = tiles.get(position);
		if (tile instanceof ITrackTile) {
			List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> list = new ArrayList<>();
			ITrackTile tt = (ITrackTile) tile;
			for (TilePath p : tt.getPaths()) {
				TileEdgeCoordinate from = new TileEdgeCoordinate(position, p._1);
				TileEdgeCoordinate to = new TileEdgeCoordinate(position, p._2);
				float cost = tt.getTravelCost();
				// if there is a one-way signal at FROM, then omit the link
				Signal s = signals.get(from);
				if (s != null && s.getType() == Type.ONE_WAY)
					continue;
				// fill list
				list.add(new T3<>(from.getOppositeDirection(), to, cost));
			}
			// list OK
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Set<Entry<TileEdgeCoordinate, Signal>> getSignals() {
		return signals.entrySet();
	}

	@Override
	public Signal getSignalAt(TileEdgeCoordinate position) {
		return signals.get(position);
	}

	@Override
	public void setSignalAt(TileEdgeCoordinate position, Signal signal) {
		if (signals.containsKey(position))
			throw new IllegalArgumentException("signal position already occupied");
		// check if signal position is valid
		Signal opponent = getSignalAt(position.getOppositeDirection());
		if (opponent != null) {
			if (opponent.getType() == Type.ONE_WAY || signal.getType() == Type.ONE_WAY)
				throw new IllegalArgumentException("signal conflict");
		}
		signals.put(position, signal);
	}

}
