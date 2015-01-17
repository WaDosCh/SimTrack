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

import java.util.AbstractMap;
import java.util.ArrayList;

import ch.awae.simtrack.model.BorderConnection.Direction;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;

/**
 * Factory class for Graph instances. This class creates a graph representation
 * of a provided set of tracks. The graph will be minimised by collapsing
 * sequential edges and dropping loose ends to improve the real-time pathfinding
 * performance by reduction of the graph dimensions.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public class GraphFactory {

	/**
	 * Constructs the graph for a given set of track pieces
	 * 
	 * @param map
	 *            the map to process
	 * @return the graph for the given track pieces
	 */
	public static Graph buildGraph(Map map) {

		Graph graph = new Graph();
		ArrayList<DirectedTileEdgeCoordinate> blacklist = new ArrayList<>();
		ArrayList<DirectedTileEdgeCoordinate> fixedDrains = new ArrayList<>();

		map.getTrackPieces().forEach(
				(pos, tile) -> {
					int u = pos.getU();
					int v = pos.getV();
					float[][] paths = tile.getRawPaths();
					if (paths == null)
						return;
					for (float[] path : paths) {
						graph.addEdge(new DirectedTileEdgeCoordinate(u, v,
								(int) path[0], false),
								new DirectedTileEdgeCoordinate(u, v,
										(int) path[1], true), path[2]);
						graph.addEdge(new DirectedTileEdgeCoordinate(u, v,
								(int) path[1], false),
								new DirectedTileEdgeCoordinate(u, v,
										(int) path[0], true), path[2]);
					}
				});
		map.getBorderTracks().forEach((pos, tile) -> {
			if (tile.getDirection() == Direction.OUT) {
				fixedDrains.add(tile.getInterfacingEdge());
			}
		});
		map.getSignals().forEach(
				(pos, sig) -> {
					if (sig.blocksInward()) {
						blacklist.add(new DirectedTileEdgeCoordinate(
								pos.getU(), pos.getV(), pos.getEdge(), false));
					}
					if (sig.blocksOutward()) {
						blacklist.add(new DirectedTileEdgeCoordinate(
								pos.getU(), pos.getV(), pos.getEdge(), true));
					}

				});
		minimise(graph, blacklist, fixedDrains);
		return graph;
	}

	private static void minimise(Graph graph,
			ArrayList<DirectedTileEdgeCoordinate> blacklist,
			ArrayList<DirectedTileEdgeCoordinate> fixedDrains) {

		blacklist.forEach(item -> graph.getMap().remove(item));

		ArrayList<AbstractMap.SimpleEntry<DirectedTileEdgeCoordinate, DirectedTileEdgeCoordinate>> toDrop = new ArrayList<>();

		do {
			toDrop.clear();
			graph.getMap().forEach((key, map) -> map.keySet().forEach(k -> {
				if (!graph.getMap().containsKey(k) && !fixedDrains.contains(k))
					toDrop.add(new AbstractMap.SimpleEntry<>(key, k));
			}));
			toDrop.forEach(item -> {
				graph.getMap().get(item.getKey()).remove(item.getValue());
				if (graph.getMap().get(item.getKey()).isEmpty()) {
					graph.getMap().remove(item.getKey());
				}
			});
		} while (!toDrop.isEmpty());
	}
}
