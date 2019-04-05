package ch.awae.simtrack.scene.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.scene.game.model.entity.Entity;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.scene.game.model.tile.DestinationTrackTile;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.TrackTile;
import ch.awae.simtrack.util.observe.Observable;
import ch.awae.simtrack.util.observe.ObservableHandler;
import ch.awae.utils.functional.T2;
import ch.awae.utils.functional.T3;
import lombok.Getter;
import lombok.NonNull;

public class Model implements Serializable, Observable, BaseTicker {

	private static final long serialVersionUID = -2351561961256044096L;
	private int sizeX, sizeY;
	private int maxS, maxT;

	private Logger logger = LogManager.getLogger();

	private HashMap<TileCoordinate, Tile> tiles = new HashMap<>();
	private HashMap<TileEdgeCoordinate, Signal> signals = new HashMap<>();
	private Set<Entity> entities = new HashSet<>();
	@Getter
	private LinkedList<PathFindingRequest> pathFindingQueue = new LinkedList<>();
	private HashMap<TileCoordinate, T2<Train, Integer>> tileReservations = new HashMap<>();
	private Set<Entity> toBeRemoved = new HashSet<>();
	private GameClock clock;
	
	@Getter
	private transient ObservableHandler observableHandler;
	private transient AtomicBoolean isPaused;


	Model(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		maxS = (int) (sizeX * new TileCoordinate(1, 0).toSceneCoordinate().s);
		maxT = (int) (sizeY * new TileCoordinate(0, 1).toSceneCoordinate().t);
	}

	public int getHorizontalSize() {
		return this.sizeX;
	}

	public int getVerticalSize() {
		return this.sizeY;
	}

	public Tile getTileAt(TileCoordinate position) {
		return this.tiles.get(position);
	}

	public void setTileAt(TileCoordinate position, Tile tile) {
		if (this.tiles.containsKey(position))
			return;
		this.tiles.put(position, tile);
		notifyChanged();
	}

	public Set<Map.Entry<TileCoordinate, Tile>> getTiles() {
		return tiles.entrySet();
	}

	public Set<Entity> getEntities() {
		return this.entities;
	}

	public void removeTileAt(TileCoordinate position) throws IllegalArgumentException {
		Tile tile = this.tiles.get(position);
		if (tile == null || tile instanceof FixedTile)
			throw new IllegalArgumentException();
		this.tiles.remove(position);
		// remove any signals
		for (Edge e : Edge.values()) {
			TileEdgeCoordinate tec = new TileEdgeCoordinate(position, e);
			if (signals.containsKey(tec))
				signals.remove(tec);
		}
		notifyChanged();
	}

	public List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> getPaths(TileCoordinate position) {
		Tile tile = tiles.get(position);
		if (tile instanceof TrackTile) {
			List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> list = new ArrayList<>();
			TrackTile tt = (TrackTile) tile;
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

	public Set<Entry<TileEdgeCoordinate, Signal>> getSignals() {
		return signals.entrySet();
	}

	public Signal getSignalAt(TileEdgeCoordinate position) {
		return signals.get(position);
	}

	public void setSignalAt(TileEdgeCoordinate position, Signal signal) {
		if (signals.containsKey(position))
			throw new IllegalArgumentException("signal position already occupied");
		// check if signal position is valid
		Tile tile = tiles.get(position.tile);
		if (tile == null || (tile instanceof DestinationTrackTile && ((DestinationTrackTile) tile).isTrainDestination())
				|| !(tile instanceof TrackTile))
			throw new IllegalArgumentException("invalid tile");
		Signal opponent = getSignalAt(position.getOppositeDirection());
		if (opponent != null) {
			if (opponent.getType() == Type.ONE_WAY || signal.getType() == Type.ONE_WAY)
				throw new IllegalArgumentException("signal conflict");
		}

		TrackTile track = (TrackTile) tile;
		if (!track.connectsAt(position.edge)) {
			throw new IllegalArgumentException("invalid edge - no connections");
		}
		signals.put(position, signal);
		notifyChanged();
	}

	public boolean canPlaceSignal(TileEdgeCoordinate position, Type type) {
		if (signals.containsKey(position))
			return false;
		// check if signal position is valid
		Tile tile = tiles.get(position.tile);
		if (tile == null || (tile instanceof DestinationTrackTile && ((DestinationTrackTile) tile).isTrainDestination())
				|| !(tile instanceof TrackTile))
			return false;
		Signal opponent = getSignalAt(position.getOppositeDirection());
		if (opponent != null) {
			if (opponent.getType() == Type.ONE_WAY || type == Type.ONE_WAY)
				return false;
		}
		// CHECK POSITION

		TrackTile track = (TrackTile) tile;
		return track.connectsAt(position.edge);
	}

	public void removeSignalAt(TileEdgeCoordinate position) {
		Signal current = signals.get(position);
		Tile raw_tile = tiles.get(position.tile);
		if (!(raw_tile instanceof TrackTile))
			throw new IllegalArgumentException("illegal tile");
		TrackTile tile = (TrackTile) raw_tile;
		if (current == null)
			throw new IllegalArgumentException("no signal exists");
		if (tile instanceof DestinationTrackTile)
			throw new IllegalArgumentException("fixed signal on soure");
		signals.remove(position);
		notifyChanged();
	}

	public boolean canRemoveSignalAt(TileEdgeCoordinate position) {
		Tile raw_tile = tiles.get(position.tile);
		if (!(raw_tile instanceof TrackTile))
			return false;
		TrackTile tile = (TrackTile) raw_tile;
		Signal current = signals.get(position);
		return !(current == null || tile instanceof DestinationTrackTile);
	}

	public void load(AtomicBoolean isPaused) {
		observableHandler = new ObservableHandler();
		this.isPaused = isPaused;
	}

	@Override
	public void tick() {
		if (this.isPaused.get())
			return;
		for (Entity entity : this.entities) {
			entity.tick();
		}
		this.entities.removeAll(this.toBeRemoved);
		this.toBeRemoved.clear();
	}

	public Set<Entry<TileCoordinate, Tile>> getTileFiltered(Function<Tile, Boolean> filter) {
		return this.tiles.entrySet().stream().filter(entry -> filter.apply(entry.getValue()))
				.collect(Collectors.toSet());
	}

	public boolean isOnMap(TileCoordinate tile) {
		SceneCoordinate P = tile.toSceneCoordinate();
		if (P.s < 0 || P.t < 0)
			return false;
		return P.s <= maxS && P.t <= maxT;
	}

	/**
	 * Releases a tile currently owned by a given train
	 * 
	 * @param train
	 *            the train that currently owns the tile
	 * @param coordinate
	 *            the tile to be released
	 * @throws IllegalArgumentException
	 *             the tile is owned by another train
	 */
	public void releaseTile(@NonNull Train train, @NonNull TileCoordinate coordinate) {
		synchronized (tileReservations) {
			T2<Train, Integer> current = tileReservations.get(coordinate);
			if (current == null)
				logger.warn(train + " tried to release an unreserved tile! " + coordinate.u + "|" + coordinate.v + "]");
			else if (!current._1.equals(train)) {
				logger.error(train + " tried to release a tile it does not own!");
				throw new IllegalArgumentException("tile ownership mismatch");
			} else if (current._2 == 1) {
				logger.debug(train + " released tile [" + coordinate.u + "|" + coordinate.v + "]");
				tileReservations.remove(coordinate);
			} else {
				logger.debug(train + " partially released tile [" + coordinate.u + "|" + coordinate.v + "]");
				tileReservations.put(coordinate, new T2<Train, Integer>(current._1, current._2 - 1));
			}
		}
	}

	/**
	 * Request ownership over a sequence of tiles
	 * 
	 * @param train
	 *            the train that wishes to reserve a bunch of tiles
	 * @param path
	 *            a sequence of tiles to be reserved
	 * @return the number of tiles that have been reserved (may be 0)
	 * @throws IllegalArgumentException
	 *             a tile in the path is no track tile
	 */
	public int reserveTiles(@NonNull Train train, @NonNull List<TileEdgeCoordinate> path) {
		synchronized (tileReservations) {
			List<TileCoordinate> block = new ArrayList<>();

			// search next signal
			for (int i = path.size() - 1; i >= 0; i--) {
				TileEdgeCoordinate edge = path.get(i);
				TileCoordinate coord = edge.tile;
				Tile tile = tiles.get(coord);
				if (!(tile instanceof TrackTile)) {
					logger.error("path element " + edge + " references an invalid tile: " + tile);
					throw new IllegalArgumentException("path element " + edge + " references an invalid tile: " + tile);
				}
				if (tileReservations.get(coord) != null) {
					if (!tileReservations.get(coord)._1.equals(train))
						return 0;
				}
				block.add(coord);
				if (signals.get(edge) != null)
					break;
			}

			// we found a signal before we hit a reservation. therefore the path
			// is clear
			for (TileCoordinate tile : block) {
				logger.debug(train + " reserved tile [" + tile.u + "|" + tile.v + "]");
				T2<Train, Integer> previous = tileReservations.get(tile);
				if (previous == null)
					tileReservations.put(tile, new T2<>(train, 1));
				else
					tileReservations.put(tile, new T2<>(train, previous._2 + 1));
			}

			return block.size();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Entity> Set<T> getEntitiesByType(Class<T> type) {
		Set<T> set = new HashSet<>();
		for (Entity e : entities) {
			if (type.isInstance(e))
				set.add((T) e);
		}
		return set;
	}

	public HashMap<TileCoordinate, T2<Train, Integer>> getTileReservations() {
		return tileReservations;
	}

	public void removeEntity(@NonNull Entity entity) {

		if (entity instanceof Train) {
			releaseAllTiles((Train) entity);
		}

		this.toBeRemoved.add(entity);
	}

	private void releaseAllTiles(Train entity) {
		synchronized (tileReservations) {
			List<TileCoordinate> matches = new ArrayList<>();
			for (Entry<TileCoordinate, T2<Train, Integer>> e : tileReservations.entrySet()) {
				if (e.getValue()._1.equals(entity)) {
					matches.add(e.getKey());
				}
			}
			for (TileCoordinate tc : matches) {
				logger.info(entity + " removed from tile [" + tc.u + "|" + tc.v + "]");
				tileReservations.remove(tc);
			}
		}
	}

	public boolean isTileReserved(TileCoordinate tile) {
		return tileReservations.get(tile) != null;
	}

	public Set<Entry<TileCoordinate, Tile>> getPossibleSpawnPoints() {
		return this.tiles.entrySet().stream().filter(this::spawnFilter).collect(Collectors.toSet());
	}

	private boolean spawnFilter(Entry<TileCoordinate, Tile> entry) {
		return entry.getValue() instanceof DestinationTrackTile
				&& ((DestinationTrackTile) entry.getValue()).isTrainSpawner() && !isTileReserved(entry.getKey());
	}

	public GameClock getClock() {
		if (clock == null)
			clock = new GameClock();
		return clock;
	}

}
