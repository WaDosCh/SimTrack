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
package ch.awae.simtrack.view;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;

/**
 * Represents the directed graph of a track network.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public class Graph {

	private HashMap<DirectedTileEdgeCoordinate, HashMap<DirectedTileEdgeCoordinate, Double>> map;

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
	 * @param cost
	 *            the transition cost. this should be a value greater or equal
	 *            to 1. Values below 1 could lead to an admissible heuristic.
	 */
	public void addEdge(DirectedTileEdgeCoordinate from,
			DirectedTileEdgeCoordinate to, double cost) {
		assert cost >= 1;
		if (this.map.containsKey(from)) {
			HashMap<DirectedTileEdgeCoordinate, Double> paths = this.map
					.get(from);
			if (!paths.containsKey(to))
				paths.put(to, cost);
		} else {
			HashMap<DirectedTileEdgeCoordinate, Double> paths = new HashMap<>();
			paths.put(to, cost);
			this.map.put(from, paths);
		}
	}

	HashMap<DirectedTileEdgeCoordinate, HashMap<DirectedTileEdgeCoordinate, Double>> getMap() {
		return this.map;
	}

	/**
	 * Retrieves all directly reachable neighbours of a given directed edge.
	 * 
	 * @param from
	 *            the edge to get the neighbours for
	 * @return the set with all destinations and their costs, or {@code null} if
	 *         the parameter {@code from} is not present in the graph or has no
	 *         neighbours.
	 */
	public Set<Entry<DirectedTileEdgeCoordinate, Double>> getNeighbours(
			DirectedTileEdgeCoordinate from) {
		if (this.map.containsKey(from))
			return this.map.get(from).entrySet();
		return null;
	}
}
