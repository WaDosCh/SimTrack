package ch.awae.simtrack.model;

public interface IDestinationTrackTile extends ITrackTile, IFixedTile {

	public boolean isTrainSpawner();

	default boolean isTrainDestination() {
		return !isTrainSpawner();
	}

}
