package ch.awae.simtrack.model.tile;

import java.io.ObjectStreamException;
import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class BasicTileProxy implements Serializable {

	private static final long serialVersionUID = 1L;

	private final TileType type;
	private final boolean fixed;

	private Object readResolve() throws ObjectStreamException {
		if (fixed)
			return BasicFixedTile.getInstance(type);
		else
			return BasicTile.getInstance(type);
	}

}
