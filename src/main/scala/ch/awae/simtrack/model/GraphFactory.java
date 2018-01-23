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
import java.util.List;

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Factory class for Graph instances. This class creates a graph representation
 * of a provided set of tracks. The graph will be minimised by collapsing
 * sequential edges and dropping loose ends to improve the real-time pathfinding
 * performance by reduction of the graph dimensions.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-23
 * @since SimTrack 0.2.1
 */
class GraphFactory {

	static Graph buildGraph(IModel model) {

		Graph graph = new Graph();
		ArrayList<DirectedTileEdgeCoordinate> blacklist = new ArrayList<>();
		ArrayList<DirectedTileEdgeCoordinate> fixedDrains = new ArrayList<>();

		model.getTiles().forEach(
				tile -> {
					TileCoordinate pos = tile.getPosition();
					int u = pos.getU();
					int v = pos.getV();
					int[] paths = tile.getRailPaths();
					if (paths == null)
						return;
					if (!tile.isFixed())
						for (int i = 0; i + 1 < paths.length; i += 2) {
							graph.addEdge(new DirectedTileEdgeCoordinate(u, v,
									paths[i], false),
									new DirectedTileEdgeCoordinate(u, v,
											paths[i + 1], true), tile);
							graph.addEdge(new DirectedTileEdgeCoordinate(u, v,
									paths[i + 1], false),
									new DirectedTileEdgeCoordinate(u, v,
											paths[i], true), tile);

						}
					else if (tile.isTrainDestination())
						fixedDrains.add(new DirectedTileEdgeCoordinate(u, v,
								paths[0], false));
				});

		model.getSignals().forEach(
				sig -> blacklist.add(sig.getPosition().getOppositeDirection()));

		model.getSignals().forEach(sig -> blacklist.remove(sig.getPosition()));

		minimise(graph, blacklist, fixedDrains);
		return graph;
	}

	private static void minimise(Graph graph,
			ArrayList<DirectedTileEdgeCoordinate> blacklist,
			ArrayList<DirectedTileEdgeCoordinate> fixedDrains) {

		for (DirectedTileEdgeCoordinate item : blacklist) {
			List<DirectedTileEdgeCoordinate> neighbours = graph
					.getNeighbours(item);
			neighbours.forEach(n -> graph.removeEdge(item, n));
		}

		ArrayList<Tuple> toDrop = new ArrayList<>();

		do {
			toDrop.clear();
			// find loose ends
			List<DirectedTileEdgeCoordinate> starts = graph.getStarts();
			starts.forEach(start -> graph.getNeighbours(start)
					.forEach(
							(end) -> {
								if (!starts.contains(end)
										&& !fixedDrains.contains(end)) {
									toDrop.add(new Tuple(start, end));
								}
							}));
			// remove loose ends
			toDrop.forEach(tuple -> graph.removeEdge(tuple.item0, tuple.item1));
		} while (!toDrop.isEmpty());
	}

	private static class Tuple {
		DirectedTileEdgeCoordinate item0, item1;

		public Tuple(DirectedTileEdgeCoordinate item0,
				DirectedTileEdgeCoordinate item1) {
			super();
			this.item0 = item0;
			this.item1 = item1;
		}

	}
}
