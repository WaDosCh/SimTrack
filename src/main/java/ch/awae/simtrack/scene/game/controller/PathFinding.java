package ch.awae.simtrack.scene.game.controller;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.PathFindingRequest;
import ch.awae.simtrack.scene.game.model.PathFindingOptions.Type;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.tile.DestinationTrackTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.util.CollectionUtil;
import ch.awae.simtrack.util.Observer;
import ch.awae.simtrack.util.T2;
import ch.awae.simtrack.util.T3;
import ch.judos.generic.data.HashMapList;
import lombok.Getter;

public class PathFinding implements BaseTicker<Game> {

	@Getter
	private Model model;

	private Logger logger = LogManager.getLogger(getClass());

	private HashMapList<TileEdgeCoordinate, T2<TileEdgeCoordinate, Float>> connectionCache;
	private Observer modelObserver;

	public PathFinding(Model model) {
		this.model = model;
		this.connectionCache = new HashMapList<>();
		this.modelObserver = this.model.createObserver();
	}

	/**
	 * forces a full rebuild of the connection graph
	 */
	private void buildGraph() {
		this.connectionCache.clear();
		for (Entry<TileCoordinate, Tile> entry : this.model.getTiles()) {
			TileCoordinate tileCoordinate = entry.getKey();
			buildGraphForTileCoordinate(tileCoordinate);
		}
	}

	private void buildGraphForTileCoordinate(TileCoordinate tileCoordinate) {
		List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> paths = this.model.getPaths(tileCoordinate);
		for (T3<TileEdgeCoordinate, TileEdgeCoordinate, Float> path : paths) {
			this.connectionCache.put(path._1, new T2<>(path._2, path._3));
		}
	}

	/**
	 * @param start
	 * @param end
	 * @return Returns a path of TileEdgeCoordinates from start (exclusive: the
	 *         train is anyway already on this edge) to end (inclusiv)<br>
	 *         Use pop operation to get the next tileEdgeCoordinate where the
	 *         train should be heading.<br>
	 *         <b>Note:</b> If there is no path, null will be returned.
	 */
	public Stack<TileEdgeCoordinate> findPath(TileEdgeCoordinate start, TileEdgeCoordinate end) {
		if (start.equals(end)) {
			logger.info("start==end for pathfinding");
			return null;
		}
		this.modelObserver.ifChanged(this::buildGraph);

		// active search elements
		ArrayList<T2<TileEdgeCoordinate, Float>> searchGraph = new ArrayList<>();
		searchGraph.add(new T2<>(start, 0f));

		// how did I get here, and how much did it cost *to* here
		HashMap<TileEdgeCoordinate, T2<TileEdgeCoordinate, Float>> wayBack = new HashMap<>();

		while (true) {
			// check outgoing paths from the best tile (shortest path + dist to
			// target)
			T2<TileEdgeCoordinate, Float> current = searchGraph.remove(0);

			// goal reached?
			if (current._1.equals(end))
				break;

			// search all paths from here
			if (this.connectionCache.containsKey(current._1)) {
				ArrayList<T2<TileEdgeCoordinate, Float>> connections = this.connectionCache.getList(current._1);
				for (T2<TileEdgeCoordinate, Float> connection : connections) {
					float cost = current._2 + connection._2;
					// only use this connection if there is no previous to this
					// edge, or this path is shorter
					if (!wayBack.containsKey(connection._1) || cost < wayBack.get(connection._1)._2) {

						wayBack.put(connection._1, new T2<>(current._1, cost));

						T2<TileEdgeCoordinate, Float> newElem = new T2<>(connection._1, current._2 + connection._2);
						searchGraph.add(newElem);
					}
				}
			}

			if (searchGraph.size() == 0)
				return null;

			Collections.sort(searchGraph, (_1, _2) -> this.searchComparator(_1, _2, end.tile));
		}

		// track back found path
		Stack<TileEdgeCoordinate> path = new Stack<>();
		TileEdgeCoordinate current = end;
		do {
			path.push(current);
			T2<TileEdgeCoordinate, Float> last = wayBack.get(current);
			if (last == null) {
				logger.error("Found target, but could not trace back to start");
				return null;
			}
			current = last._1;
		} while (!current.equals(start));
		return path;
	}

	/**
	 * cheapests (not performance!) implementation ever!
	 * 
	 * @param start
	 * @param possibleTargets
	 * @return
	 */
	public HashMap<TileEdgeCoordinate, Stack<TileEdgeCoordinate>> findPathForTiles(TileEdgeCoordinate start,
			List<TileEdgeCoordinate> possibleTargets) {
		HashMap<TileEdgeCoordinate, Stack<TileEdgeCoordinate>> result = new HashMap<>();
		for (TileEdgeCoordinate target : possibleTargets) {
			Stack<TileEdgeCoordinate> path = findPath(start, target);
			if (path != null) {
				result.put(target, path);
			}
		}
		return result;
	}

	public int searchComparator(T2<TileEdgeCoordinate, Float> arg0, T2<TileEdgeCoordinate, Float> arg1,
			TileCoordinate target) {
		double dist1 = arg0._2 + arg0._1.tile.distanceTo(target);
		double dist2 = arg1._2 + arg1._1.tile.distanceTo(target);
		return (int) (dist1 - dist2);
	}

	public void setModel(Model model) {
		this.model = model;
		this.modelObserver = this.model.createObserver();
	}

	public void tick() {
		int maxWorkPerTick = 5;
		LinkedList<PathFindingRequest> queue = this.model.getPathFindingQueue();
		while (maxWorkPerTick-- > 0 && queue.size() > 0) {
			PathFindingRequest request = queue.removeFirst();
			if (request.options.type == Type.RandomTarget) {
				Stack<TileEdgeCoordinate> path = randomPathForStart(request.start);
				if (path != null) {
					request.pathAcceptor.accept(path);
				} else {
					logger.warn("No path available for start position:" + request.start);
					// queue.addLast(request);
				}
			} else {
				logger.error("Unknown PathFinding option type:", request.options.type);
			}
		}
	}

	public Stack<TileEdgeCoordinate> randomPathForStart(TileEdgeCoordinate start) {
		Set<Entry<TileCoordinate, Tile>> destinations = this.model.getTileFiltered(
				tile -> tile instanceof DestinationTrackTile && ((DestinationTrackTile) tile).isTrainDestination());
		List<TileEdgeCoordinate> targets = destinations.stream()
				.map(destination -> this.model.getPaths(destination.getKey()).get(0)._2).collect(Collectors.toList());

		HashMap<TileEdgeCoordinate, Stack<TileEdgeCoordinate>> paths = findPathForTiles(start, targets);
		if (paths.size() == 0)
			return null;
		Entry<TileEdgeCoordinate, Stack<TileEdgeCoordinate>> randomPath = CollectionUtil.randomValue(paths.entrySet());
		return randomPath.getValue();
	}

	@Override
	public void tick(Game scene) {
		this.tick();
	}
}