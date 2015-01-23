package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.TileCoordinate;

public interface ITrain extends IEntity {

	public TileCoordinate getPosition();

	public TileCoordinate getOrigin();

	public TileCoordinate getDestination();

	public State getState();

	public static enum State {
		MOVING, WAITING, FINISHED, NOPATH, ERROR;
	}

}
