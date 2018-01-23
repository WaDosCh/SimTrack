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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;

/**
 * Represents the directed graph of a track network.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
class Graph implements IGraph<DirectedTileEdgeCoordinate, ITile> {

	private HashMap<DirectedTileEdgeCoordinate, HashMap<DirectedTileEdgeCoordinate, ITile>> map;

	{
		this.map = new HashMap<>();
	}

	/**
	 * Adds an edge with defined starting point and destination and its cost to
	 * the graph. If the edge is already present, it will not be stored again.
	 * 
	 * @param from
	 *            the starting edge
	 * @param to
	 *            the destination edge
	 */
	@Override
	public void addEdge(DirectedTileEdgeCoordinate from,
			DirectedTileEdgeCoordinate to, ITile tile) {
		assert tile.getTravelCost() >= 1;
		if (this.map.containsKey(from)) {
			HashMap<DirectedTileEdgeCoordinate, ITile> paths = this.map
					.get(from);
			if (!paths.containsKey(to))
				paths.put(to, tile);
		} else {
			HashMap<DirectedTileEdgeCoordinate, ITile> paths = new HashMap<>();
			paths.put(to, tile);
			this.map.put(from, paths);
		}
	}

	@Override
	public double getCost(DirectedTileEdgeCoordinate start,
			DirectedTileEdgeCoordinate end) {
		if (this.map.containsKey(start) && this.map.get(start).containsKey(end))
			return this.map.get(start).get(end).getTravelCost();
		return Double.MAX_VALUE;
	}

	@Override
	public void removeEdge(DirectedTileEdgeCoordinate start,
			DirectedTileEdgeCoordinate end) {
		if (!this.map.containsKey(start))
			return;
		this.map.get(start).remove(end);
		if (this.map.get(start).isEmpty())
			this.map.remove(start);
	}

	List<DirectedTileEdgeCoordinate> getStarts() {
		return new ArrayList<>(this.map.keySet());
	}

	@Override
	public ITile getEdge(DirectedTileEdgeCoordinate start,
			DirectedTileEdgeCoordinate end) {
		if (this.map.containsKey(start))
			return this.map.get(start).getOrDefault(end, null);
		return null;
	}

	@Override
	public List<DirectedTileEdgeCoordinate> getNeighbours(
			DirectedTileEdgeCoordinate node) {
		if (this.map.containsKey(node))
			return new ArrayList<>(this.map.get(node).keySet());
		return null;
	}

}
