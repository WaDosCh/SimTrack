package ch.awae.simtrack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import ch.awae.simtrack.model.BasicTrackTile;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.util.T3;

public class PathFinding {

	private IModel model;
	private HashMap<TileEdgeCoordinate, TileEdgeCoordinate> connectionCache;

	public PathFinding(IModel model) {
		this.model = model;
		this.connectionCache = new HashMap<TileEdgeCoordinate, TileEdgeCoordinate>();
	}

	/**
	 * forces a full rebuild of the connection graph
	 */
	public void buildGraph() {
		this.connectionCache.clear();
		for (Entry<TileCoordinate, ITile> entry : this.model.getTiles()) {
			ITile tile = entry.getValue();
			TileCoordinate tileCoordinate = entry.getKey();
			if (tile instanceof BasicTrackTile) {
				buildGraphForTileCoordinate(tileCoordinate);
			}
		}
	}

	private void buildGraphForTileCoordinate(TileCoordinate tileCoordinate) {
		List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> paths = this.model
				.getPaths(tileCoordinate);
		for (T3<TileEdgeCoordinate, TileEdgeCoordinate, Float> path : paths) {

		}
	}

	/**
	 * @param start
	 * @param end
	 * @return returns a path of TileEdgeCoordinates from start (exclusive: the
	 *         train is anyway already on this edge) to end (inclusiv)<br>
	 *         Use pop operation to get the next tileEdgeCoordinate where the
	 *         train should be heading
	 */
	public Stack<TileEdgeCoordinate> findPath(TileEdgeCoordinate start,
			TileEdgeCoordinate end) {

		Stack<TileEdgeCoordinate> path = new Stack<>();
		return path;
	}
}
