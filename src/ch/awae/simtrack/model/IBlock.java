package ch.awae.simtrack.model;

import java.util.List;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IBlock extends IEntity {

	public void merge(IBlock block);

	public List<ITile> getTiles();

	public List<ISignal> getEntrySignals();

	public List<ISignal> getExitSignals();

	public boolean hasOpenExitPresignal();

	public boolean isOccupied();

	public boolean isReserved();

	public void occupy(ITrain occupant);

	public ITrain getOccupant();

	public ITrain getReserver();

	public void reserve(ITrain reserver);

	public void free();

}
