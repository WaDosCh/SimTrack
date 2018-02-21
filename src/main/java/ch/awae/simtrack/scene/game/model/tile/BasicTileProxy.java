package ch.awae.simtrack.scene.game.model.tile;

import java.io.ObjectStreamException;

import ch.awae.simtrack.util.serial.SerializationProxy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class BasicTileProxy implements SerializationProxy {

	private static final long serialVersionUID = 1L;

	private final TileType type;
	private final boolean fixed;

	public Object readResolve() throws ObjectStreamException {
		System.out.println("resolve " + type + "(" + fixed + ")");
		if (fixed)
			return BasicFixedTile.getInstance(type);
		else
			return BasicTile.getInstance(type);
	}

}
