package ch.awae.simtrack.model;

import java.util.List;

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IModel {

	public int getHorizontalSize();

	public int getVerticalSize();

	public ITile getTileAt(TileCoordinate position);

	public void setTileAt(TileCoordinate position, ITile tile);

	public List<ITile> getTiles();

	public void removeTileAt(TileCoordinate position)
			throws IllegalArgumentException;

	public ISignal getSignalAt(DirectedTileEdgeCoordinate position);

	public void setSignalAt(DirectedTileEdgeCoordinate position, ISignal signal);

	public List<ISignal> getSignals();

	public void removeSignalAt(DirectedTileEdgeCoordinate position)
			throws IllegalArgumentException;

	public IGraph<DirectedTileEdgeCoordinate, ITile> getGraph();

	public ITrain getTrainAt(TileCoordinate position);

	public List<ITrain> getTrains();

	public void addTrain(ITrain train);

	public void removeTrain(ITrain train) throws IllegalArgumentException;
	
	public void update();
	
	public void tick();

}
