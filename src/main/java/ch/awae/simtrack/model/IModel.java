package ch.awae.simtrack.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.util.IObservable;
import ch.awae.simtrack.util.T3;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IModel extends IObservable, Serializable {

	public Set<IEntity> getEntities();

	public int getHorizontalSize();

	public int getVerticalSize();

	public ITile getTileAt(TileCoordinate position);

	public void setTileAt(TileCoordinate position, ITile tile);

	public Set<Map.Entry<TileCoordinate, ITile>> getTiles();

	public Set<Entry<TileCoordinate, ITile>> getTileFiltered(Function<ITile, Boolean> filter);

	public void removeTileAt(TileCoordinate position) throws IllegalArgumentException;

	public LinkedList<PathFindingRequest> getPathFindingQueue();

	public void tick();

	public List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> getPaths(TileCoordinate position);

	public Set<Map.Entry<TileEdgeCoordinate, Signal>> getSignals();

	Signal getSignalAt(TileEdgeCoordinate position);

	void setSignalAt(TileEdgeCoordinate position, Signal signal);

	void removeSignalAt(TileEdgeCoordinate position);

	/**
	 * called when a model is created or loaded from file.
	 */
	public void load();

}
