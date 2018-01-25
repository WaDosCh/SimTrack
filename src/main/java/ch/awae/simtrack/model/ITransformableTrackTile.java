package ch.awae.simtrack.model;

public interface ITransformableTrackTile extends ITrackTile {

	public ITransformableTrackTile cloneTile();

	public ITransformableTrackTile rotated(boolean clockwise);

	public ITransformableTrackTile mirrored();

}
