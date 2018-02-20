package ch.awae.simtrack.model.tile;

public interface DestinationTrackTile extends TrackTile, FixedTile {

	public boolean isTrainSpawner();

	default boolean isTrainDestination() {
		return !isTrainSpawner();
	}

}
