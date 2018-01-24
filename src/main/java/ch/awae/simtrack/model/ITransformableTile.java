package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.TileCoordinate;

public interface ITransformableTile extends ITile {

	public ITransformableTile cloneTile();

	public ITransformableTile rotated(boolean clockwise);

	public ITransformableTile mirrored();

	public ITransformableTile withPosition(TileCoordinate position);

}
