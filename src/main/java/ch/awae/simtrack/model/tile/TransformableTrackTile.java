package ch.awae.simtrack.model.tile;

public interface TransformableTrackTile extends TrackTile {

	public TransformableTrackTile cloneTile();

	public TransformableTrackTile rotated(boolean clockwise);

	public TransformableTrackTile mirrored();

}
