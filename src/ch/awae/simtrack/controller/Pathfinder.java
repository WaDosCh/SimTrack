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
package ch.awae.simtrack.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

import ch.awae.simtrack.model.Graph;
import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;

/**
 * A-Star pathfinding algorithm.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-23
 * @since SimTrack 0.1.2
 */
public class Pathfinder {

	private static class PriorityEntry implements Comparable<PriorityEntry> {

		DirectedTileEdgeCoordinate element;

		double priority;

		PriorityEntry(double prio, DirectedTileEdgeCoordinate elem) {
			this.priority = prio;
			this.element = elem;
		}

		@Override
		public int compareTo(PriorityEntry o) {
			return Double.compare(this.priority, o.priority);
		}

	}

	private static double heuristic(DirectedTileEdgeCoordinate goal,
			DirectedTileEdgeCoordinate now) {
		return goal.distanceTo(now);
	}

	public ArrayList<DirectedTileEdgeCoordinate> findPath(
			DirectedTileEdgeCoordinate from, DirectedTileEdgeCoordinate to,
			Graph graph) {
		PriorityQueue<PriorityEntry> frontier = new PriorityQueue<>();
		HashMap<DirectedTileEdgeCoordinate, DirectedTileEdgeCoordinate> cameFrom = new HashMap<>();
		HashMap<DirectedTileEdgeCoordinate, Double> costSoFar = new HashMap<>();

		frontier.add(new PriorityEntry(0, from));
		costSoFar.put(from, 0.0);

		while (!frontier.isEmpty()) {
			PriorityEntry current = frontier.poll();
			if (current.element.equals(to))
				break;
			for (DirectedTileEdgeCoordinate next : graph
					.getNeighbours(current.element)) {
				double cost = graph.getCost(current.element, next);
				double newCost = costSoFar.get(current).doubleValue() + cost;
				if (!costSoFar.containsKey(next)
						|| newCost < costSoFar.get(next).doubleValue()) {
					costSoFar.put(next, newCost);
					double priority = newCost + heuristic(to, next);
					frontier.add(new PriorityEntry(priority, next));
					cameFrom.put(next, current.element);
				}
			}
		}

		// POST-PROCESSING: DETERMINE PATH FROM BACKTRACES

		ArrayList<DirectedTileEdgeCoordinate> path = new ArrayList<>();

		DirectedTileEdgeCoordinate current = to;
		path.add(current);
		while (!current.equals(from)) {
			current = cameFrom.get(current);
			path.add(current);
		}

		Collections.reverse(path);
		return path;

	}
}
