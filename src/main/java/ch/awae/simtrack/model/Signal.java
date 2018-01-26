package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import lombok.Getter;

public class Signal implements IEntity {

	public enum Type {
		TWO_WAY, ONE_WAY;
	}

	@Getter
	private final TileEdgeCoordinate position;
	@Getter
	private final Type type;

	public Signal(TileEdgeCoordinate position, Type type) {
		this.position = position;
		this.type = type;
	}

}
