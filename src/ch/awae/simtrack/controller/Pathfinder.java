package ch.awae.simtrack.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;
import ch.awae.simtrack.view.Graph;

public class Pathfinder {

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
			for (Entry<DirectedTileEdgeCoordinate, Double> entry : graph
					.getNeighbours(current.element)) {
				DirectedTileEdgeCoordinate next = entry.getKey();
				double cost = entry.getValue().doubleValue();
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

	private static double heuristic(DirectedTileEdgeCoordinate goal,
			DirectedTileEdgeCoordinate now) {
		return goal.distanceTo(now);
	}

	private static class PriorityEntry implements Comparable<PriorityEntry> {

		PriorityEntry(double prio, DirectedTileEdgeCoordinate elem) {
			this.priority = prio;
			this.element = elem;
		}

		double priority;
		DirectedTileEdgeCoordinate element;

		@Override
		public int compareTo(PriorityEntry o) {
			return Double.compare(this.priority, o.priority);
		}

	}
}
