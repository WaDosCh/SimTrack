package ch.awae.simtrack.scene.game.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.PathFindingOptions.Type;
import ch.awae.simtrack.scene.game.model.PathFindingRequest;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.position.TilePathCoordinate;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.util.CollectionUtil;
import ch.awae.simtrack.util.observe.Observer;
import ch.awae.utils.pathfinding.DijkstraPathfinder;
import ch.awae.utils.pathfinding.GraphDataProvider;
import ch.awae.utils.pathfinding.Pathfinder;
import ch.judos.generic.data.HashMap2;
import lombok.Getter;
import lombok.val;

public class PathFinding implements BaseTicker, GraphDataProvider<TileEdgeCoordinate> {

	@Getter
	private Model model;

	private Logger logger = LogManager.getLogger(getClass());

	private HashMap2<TileEdgeCoordinate, TileEdgeCoordinate, Double> connectionCache;
	private Observer modelObserver;
	private Pathfinder<TileEdgeCoordinate> pathfinder;

	public PathFinding(Model model) {
		this.model = model;
		this.connectionCache = new HashMap2<>();
		this.modelObserver = this.model.createObserver();
		this.pathfinder = new DijkstraPathfinder<>(this);
	}

	/**
	 * forces a full rebuild of the connection graph
	 */
	private void buildGraph() {
		this.connectionCache.clear();
		for (Entry<TileCoordinate, Tile> entry : this.model.getTilesSet()) {
			TileCoordinate tileCoordinate = entry.getKey();
			buildGraphForTileCoordinate(tileCoordinate);
		}
	}

	private void buildGraphForTileCoordinate(TileCoordinate tileCoordinate) {
		for (TilePathCoordinate path : this.model.getPaths(tileCoordinate)) {
			this.connectionCache.put(path.getFrom().getOppositeDirection(), path.getTo(), path.getPathLength());
		}
	}

	/**
	 * @param start
	 * @param end
	 * @return Returns a path of TileEdgeCoordinates from start (exclusive: the train is anyway already on this edge) to
	 *         end (inclusive)<br>
	 *         Use pop operation to get the next tileEdgeCoordinate where the train should be heading.<br>
	 *         <b>Note:</b> If there is no path, null will be returned.
	 */
	public Stack<TileEdgeCoordinate> findPath(TileEdgeCoordinate start, TileEdgeCoordinate end) {
		if (start.equals(end)) {
			logger.info("start==end for pathfinding");
			return null;
		}
		this.modelObserver.ifChanged(this::buildGraph);

		val result = this.pathfinder.execute(start, end);

		switch (result.getType()) {
			case SUCCESS:
				logger.debug("found a path of length " + result.getPath().size() + " (steps: " + result.getSearchSteps()
						+ ", time: " + result.getSearchTime() + "ms).");
				// the result path may be a list but is in reverse order
				val stack = new Stack<TileEdgeCoordinate>();
				stack.addAll(result.getPath());
				return stack;
			case FAILURE:
				logger.debug("no path found (steps: " + result.getSearchSteps() + ", time: " + result.getSearchTime()
						+ "ms)!");
				return null;
			case TIMEOUT:
				logger.warn("pathfinder timeout");
				return null;
			default:
				throw new AssertionError();
		}
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

	@Override
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
					request.noPathFound.run();
				}
			} else {
				logger.error("Unknown PathFinding option type:", request.options.type);
			}
		}
	}

	public Stack<TileEdgeCoordinate> randomPathForStart(TileEdgeCoordinate start) {
		Set<Entry<TileCoordinate, BorderTrackTile>> destinations = this.model.getTileFiltered(BorderTrackTile.class,
				t -> t.isTrainDestination());
		List<TileEdgeCoordinate> targets = destinations.stream()
				.map(destination -> this.model.getPaths(destination.getKey()).get(0).getTo())
				.collect(Collectors.toList());

		HashMap<TileEdgeCoordinate, Stack<TileEdgeCoordinate>> paths = findPathForTiles(start, targets);
		if (paths.size() == 0)
			return null;
		Entry<TileEdgeCoordinate, Stack<TileEdgeCoordinate>> randomPath = CollectionUtil.randomValue(paths.entrySet());
		return randomPath.getValue();
	}

	@Override
	public Iterable<TileEdgeCoordinate> getNeighbours(TileEdgeCoordinate vertex) {
		return this.connectionCache.getInnerKeySet(vertex);
	}

	@Override
	public double getDistance(TileEdgeCoordinate from, TileEdgeCoordinate to) {
		return this.connectionCache.get(from, to);
	}

}

