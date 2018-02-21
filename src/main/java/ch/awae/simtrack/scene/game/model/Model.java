package ch.awae.simtrack.scene.game.model;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.awae.simtrack.scene.game.model.entity.Entity;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.scene.game.model.tile.DestinationTrackTile;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.TrackTile;
import ch.awae.simtrack.util.Observable;
import ch.awae.simtrack.util.ObservableHandler;
import ch.awae.simtrack.util.T3;
import lombok.Getter;

public class Model implements Serializable, Observable {

	private static final long serialVersionUID = -2351561961256044096L;
	private int sizeX, sizeY;
	private int maxS, maxT;

	private HashMap<TileCoordinate, Tile> tiles = new HashMap<>();
	private HashMap<TileEdgeCoordinate, Signal> signals = new HashMap<>();
	private Set<Entity> entities = new HashSet<>();
	@Getter
	private LinkedList<PathFindingRequest> pathFindingQueue = new LinkedList<>();
	@Getter
	private transient ObservableHandler observableHandler;

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
		TrackTile tile = (TrackTile) tiles.get(position.tile);
		if (current == null)
			throw new IllegalArgumentException("no signal exists");
		if (tile instanceof DestinationTrackTile)
			throw new IllegalArgumentException("fixed signal on soure");
		signals.remove(position);
		notifyChanged();
	}

	public boolean canRemoveSignalAt(TileEdgeCoordinate position) {
		TrackTile tile = (TrackTile) tiles.get(position.tile);
		Signal current = signals.get(position);
		return !(current == null || tile instanceof DestinationTrackTile);
	}

	public void load() {
		observableHandler = new ObservableHandler();
	}

	public void tick() {
		for (Entity entity : this.entities) {
			entity.tick((pathFindingRequest) -> this.pathFindingQueue.addLast(pathFindingRequest));
		}
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

}
