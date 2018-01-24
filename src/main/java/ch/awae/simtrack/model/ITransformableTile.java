package ch.awae.simtrack.model;

public interface ITransformableTile extends ITile {

	public ITransformableTile cloneTile();

	public ITransformableTile rotated(boolean clockwise);

	public ITransformableTile mirrored();

}
