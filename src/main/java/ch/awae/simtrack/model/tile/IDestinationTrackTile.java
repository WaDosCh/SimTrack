package ch.awae.simtrack.model.tile;

public interface IDestinationTrackTile extends ITrackTile, IFixedTile {

	public boolean isTrainSpawner();

	default boolean isTrainDestination() {
		return !isTrainSpawner();
	}

}
