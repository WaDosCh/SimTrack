package ch.awae.simtrack.scene.game.model;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
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
import ch.awae.simtrack.scene.game.model.position.TilePathCoordinate;
import ch.awae.simtrack.scene.game.model.tile.BulldozeTile;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.UpgradeTile;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;
import ch.awae.simtrack.util.observe.Observable;
import ch.awae.simtrack.util.observe.ObservableHandler;
import ch.awae.utils.functional.T2;
import lombok.Getter;
import lombok.NonNull;

public class Model implements Serializable, Observable, BaseTicker {

	private static final long serialVersionUID = -2351561961256044096L;
	private Logger logger = LogManager.getLogger();
	private @Getter Dimension tileGridSize;
	private int maxS, maxT;

	private HashMap<TileCoordinate, Tile> tiles = new HashMap<>();
	private HashMap<TileEdgeCoordinate, Signal> signals = new HashMap<>();

	private Set<Entity> entities = new HashSet<>();
	private HashMap<TileCoordinate, T2<Train, Integer>> tileReservations = new HashMap<>();
	private Set<Entity> toBeRemoved = new HashSet<>();

	public int playerMoney;
	private @Getter int bulldozeCost;

	private @Getter GameClock clock = new GameClock();
	private @Getter LinkedList<PathFindingRequest> pathFindingQueue = new LinkedList<>();
	private @Getter AtomicBoolean isPaused = new AtomicBoolean(false);
	private @Getter AtomicBoolean drawGrid = new AtomicBoolean(true);

	private @Getter DebugOptions debugOptions = new DebugOptions();
	private @Getter ViewPortData viewPortData = new ViewPortData();
	public String lastSaveGameName = "NewCustomGame";

	private @Getter transient ObservableHandler observableHandler;

	Model(Dimension size, int startingMoney, int bulldozeCost) {
		this.tileGridSize = size;
		this.playerMoney = startingMoney;
		this.bulldozeCost = bulldozeCost;
		this.maxS = this.tileGridSize.width * TileCoordinate.TILE_U_S_OFFSET;
		this.maxT = this.tileGridSize.height * TileCoordinate.TILE_V_T_OFFSET;
	}

	public Tile getTileAt(TileCoordinate position) {
		return this.tiles.get(position);
	}

	public void setTileAt(TileCoordinate position, Tile tile) {
		Tile oldTile = this.tiles.get(position);
		if (oldTile != null && !(oldTile instanceof BulldozeTile))
			return;
		if (oldTile instanceof BulldozeTile && tile instanceof TrackTile) {
			oldTile = ((BulldozeTile) oldTile).getTile();
			tile = new UpgradeTile(oldTile, (TrackTile) tile);
		}
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

		// if tile is still reserved replace it with a BulldozeTile
		if (this.tileReservations.get(position) != null) {
			this.tiles.put(position, new BulldozeTile(tile));
			notifyChanged();
			return;
		}

		this.tiles.remove(position);
		// remove any signals
		for (Edge e : Edge.values()) {
			signals.remove(position.getEdge(e));
		}
		notifyChanged();
	}

	public List<TilePathCoordinate> getPaths(TileCoordinate position) {
		Tile tile = tiles.get(position);
		if (tile instanceof TrackTile) {
			TrackTile tt = (TrackTile) tile;
			List<TilePathCoordinate> list = tt.getAllDirectedPaths(position);
			list = list.stream().filter(x -> {
				Signal s = signals.get(x.getFrom());
				// if there is a one-way signal at FROM, then omit the link
				return s == null || s.getType() != Type.ONE_WAY;
			}).collect(Collectors.toList());
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	public Set<Entry<TileEdgeCoordinate, Signal>> getSignals() {
		return this.signals.entrySet();
	}

	public Signal getSignalAt(TileEdgeCoordinate position) {
		return this.signals.get(position);
	}

	public boolean setSignalAt(TileEdgeCoordinate position, Signal signal) {
		if (!canPlaceSignal(position, signal.getType()))
			return false;
		signals.put(position, signal);
		notifyChanged();
		return true;
	}

	public boolean canPlaceSignal(TileEdgeCoordinate position, Type type) {
		if (signals.containsKey(position))
			return false;
		// check if signal position is valid
		Tile tile = tiles.get(position.tile);
		if (tile == null || (tile instanceof BorderTrackTile && ((BorderTrackTile) tile).isTrainDestination())
				|| !(tile instanceof TrackTile))
			return false;
		Signal opponent = getSignalAt(position.getOppositeDirection());
		if (opponent != null) {
			if (opponent.getType() == Type.ONE_WAY || type == Type.ONE_WAY)
				return false;
		}
		TrackTile track = (TrackTile) tile;
		return track.connectsAt(position.edge);
	}

	public void removeSignalAt(TileEdgeCoordinate position) {
		Tile raw_tile = tiles.get(position.tile);
		if (!(raw_tile instanceof TrackTile))
			throw new IllegalArgumentException("illegal tile");
		TrackTile tile = (TrackTile) raw_tile;
		if (tile instanceof BorderTrackTile)
			throw new IllegalArgumentException("fixed signal on soure");
		if (signals.remove(position) != null)
			notifyChanged();
	}

	public boolean canRemoveSignalAt(TileEdgeCoordinate position) {
		Tile raw_tile = tiles.get(position.tile);
		if (!(raw_tile instanceof TrackTile))
			return false;
		TrackTile tile = (TrackTile) raw_tile;
		Signal current = signals.get(position);
		return !(current == null || tile instanceof BorderTrackTile);
	}

	public void load() {
		this.observableHandler = new ObservableHandler();
		this.isPaused.set(false);
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

	public <T extends Tile> Set<Entry<TileCoordinate, T>> getTileFiltered(Class<T> clazz) {
		return getTileFiltered(clazz, (t) -> true);
	}

	@SuppressWarnings("unchecked")
	public <T extends Tile> Set<Entry<TileCoordinate, T>> getTileFiltered(Class<T> clazz, Function<T, Boolean> filter) {
		return this.tiles.entrySet().stream().filter(tile -> {
			return clazz.isInstance(tile.getValue()) && filter.apply((T) tile.getValue());
		}).map(tile -> new SimpleEntry<TileCoordinate, T>(tile.getKey(), (T) tile.getValue()))
				.collect(Collectors.toSet());
	}

	public boolean isOnMap(TileCoordinate tile) {
		SceneCoordinate sceneCoord = tile.toSceneCoordinate();
		if (sceneCoord.s < 0 || sceneCoord.t < 0)
			return false;
		return sceneCoord.s < maxS && sceneCoord.t < maxT;
	}

	/**
	 * Releases a tile
	 * 
	 * @param entity the entity that wants to release the tile
	 * @param coordinate the tile to be released
	 * @throws IllegalArgumentException the tile is not owned by the given entity
	 */
	public void releaseTile(@NonNull Entity entity, @NonNull TileCoordinate coordinate) {
		synchronized (tileReservations) {
			T2<Train, Integer> current = tileReservations.get(coordinate);
			if (current == null)
				logger.warn(
						entity + " tried to release an unreserved tile! " + coordinate.u + "|" + coordinate.v + "]");
			else if (!current._1.equals(entity)) {
				logger.error(entity + " tried to release a tile it does not own!");
				throw new IllegalArgumentException("tile ownership mismatch");
			} else if (current._2 == 1) {
				logger.debug(entity + " released tile [" + coordinate.u + "|" + coordinate.v + "]");
				tileReservations.remove(coordinate);
				Tile tile = this.tiles.get(coordinate);
				if (tile instanceof BulldozeTile) {
					this.tiles.remove(coordinate);
					for (Edge e : Edge.values())
						this.signals.remove(coordinate.getEdge(e));
					notifyChanged();
				} else if (tile instanceof UpgradeTile) {
					this.tiles.put(coordinate, ((UpgradeTile) tile).getToBeBuilt());
					notifyChanged();
				}

			} else {
				logger.debug(entity + " partially released tile [" + coordinate.u + "|" + coordinate.v + "]");
				tileReservations.put(coordinate, new T2<Train, Integer>(current._1, current._2 - 1));
			}
		}
	}

	/**
	 * Request ownership over a sequence of tiles<br>
	 * XXX: move this into a controller, model should just provide signals and path of TrackTiles not individual logic
	 * 
	 * @param train the train that wishes to reserve a bunch of tiles
	 * @param path a sequence of tiles to be reserved
	 * @return the number of tiles that have been reserved (may be 0)<br>
	 *         might return -1 when the route has become invalid due to missing tracks
	 * @throws IllegalArgumentException a tile in the path is no track tile
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
					logger.warn("path element " + edge + " references an invalid tile: " + tile);
					return -1;
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
		releaseTiles(entity, entity.getReservedTiles());
		this.toBeRemoved.add(entity);
	}

	private void releaseTiles(Entity entity, Set<TileCoordinate> releaseTiles) {
		if (releaseTiles == null)
			return;
		for (TileCoordinate tile : releaseTiles) {
			releaseTile(entity, tile);
		}
	}

	public boolean isTileReserved(TileCoordinate tile) {
		return tileReservations.get(tile) != null;
	}

}
