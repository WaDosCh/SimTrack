package ch.awae.simtrack.model.tile;

public interface ITransformableTrackTile extends ITrackTile {

	public ITransformableTrackTile cloneTile();

	public ITransformableTrackTile rotated(boolean clockwise);

	public ITransformableTrackTile mirrored();

}
