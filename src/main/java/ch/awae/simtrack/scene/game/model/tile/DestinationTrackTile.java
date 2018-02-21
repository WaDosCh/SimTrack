package ch.awae.simtrack.scene.game.model.tile;

public interface DestinationTrackTile extends TrackTile, FixedTile {

	public boolean isTrainSpawner();

	default boolean isTrainDestination() {
		return !isTrainSpawner();
	}

}
