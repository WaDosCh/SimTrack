package ch.awae.simtrack.model;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.awae.simtrack.model.entity.IEntity;
import ch.awae.simtrack.model.entity.Signal;
import ch.awae.simtrack.model.entity.Signal.Type;
import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.model.position.TilePath;
import ch.awae.simtrack.model.tile.IDestinationTrackTile;
import ch.awae.simtrack.model.tile.IFixedTile;
import ch.awae.simtrack.model.tile.ITile;
import ch.awae.simtrack.model.tile.ITrackTile;
import ch.awae.simtrack.util.ObservableHandler;
import ch.awae.simtrack.util.T3;
import lombok.Getter;

class Model implements IModel {

	private static final long serialVersionUID = -2351561961256044096L;
	private int sizeX, sizeY;

	private HashMap<TileCoordinate, ITile> tiles = new HashMap<>();
	private HashMap<TileEdgeCoordinate, Signal> signals = new HashMap<>();
	private Set<IEntity> entities = new HashSet<>();
	@Getter
	private LinkedList<PathFindingRequest> pathFindingQueue = new LinkedList<>();
	@Getter
	private transient ObservableHandler observableHandler;

	Model(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	@Override
	public int getHorizontalSize() {
		return this.sizeX;
	}

	@Override
	public int getVerticalSize() {
		return this.sizeY;
	}

	@Override
	public ITile getTileAt(TileCoordinate position) {
		return this.tiles.get(position);
	}

	@Override
	public void setTileAt(TileCoordinate position, ITile tile) {
		if (this.tiles.containsKey(position))
			return;
		this.tiles.put(position, tile);
		notifyChanged();
	}

	@Override
	public Set<Map.Entry<TileCoordinate, ITile>> getTiles() {
		return tiles.entrySet();
	}

	public Set<IEntity> getEntities() {
		return this.entities;
	}

	@Override
	public void removeTileAt(TileCoordinate position) throws IllegalArgumentException {
		ITile tile = this.tiles.get(position);
		if (tile == null || tile instanceof IFixedTile)
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

	@Override
	public List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> getPaths(TileCoordinate position) {
		ITile tile = tiles.get(position);
		if (tile instanceof ITrackTile) {
			List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> list = new ArrayList<>();
			ITrackTile tt = (ITrackTile) tile;
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

	@Override
	public Set<Entry<TileEdgeCoordinate, Signal>> getSignals() {
		return signals.entrySet();
	}

	@Override
	public Signal getSignalAt(TileEdgeCoordinate position) {
		return signals.get(position);
	}

	@Override
	public void setSignalAt(TileEdgeCoordinate position, Signal signal) {
		if (signals.containsKey(position))
			throw new IllegalArgumentException("signal position already occupied");
		// check if signal position is valid
		ITile tile = tiles.get(position.tile);
		if (tile == null
				|| (tile instanceof IDestinationTrackTile && ((IDestinationTrackTile) tile).isTrainDestination())
				|| !(tile instanceof ITrackTile))
			throw new IllegalArgumentException("invalid tile");
		Signal opponent = getSignalAt(position.getOppositeDirection());
		if (opponent != null) {
			if (opponent.getType() == Type.ONE_WAY || signal.getType() == Type.ONE_WAY)
				throw new IllegalArgumentException("signal conflict");
		}
		signals.put(position, signal);
		notifyChanged();
	}

	@Override
	public void removeSignalAt(TileEdgeCoordinate position) {
		Signal current = signals.get(position);
		ITrackTile tile = (ITrackTile) tiles.get(position.tile);
		if (current == null || tile instanceof IDestinationTrackTile)
			throw new IllegalArgumentException();
		signals.remove(position);
		notifyChanged();
	}

	@Override
	public void load() {
		observableHandler = new ObservableHandler();
	}

	@Override
	public void tick() {
		for (IEntity entity : this.entities) {
			entity.tick((pathFindingRequest) -> this.pathFindingQueue.addLast(pathFindingRequest));
		}
	}

	@Override
	public Set<Entry<TileCoordinate, ITile>> getTileFiltered(Function<ITile, Boolean> filter) {
		return this.tiles.entrySet().stream().filter(entry -> filter.apply(entry.getValue()))
				.collect(Collectors.toSet());
	}

}
