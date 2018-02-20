package ch.awae.simtrack.model.tile;

import java.io.ObjectStreamException;

import lombok.Getter;

/**
 * Class managing basic tiles.
 * 
 * A basic fixed tile is a simple tile instance fully defined by its type that
 * cannot be removed. For each TileType a single instance is provided.
 * 
 * @see #getInstance(TileType)
 */
public final class BasicFixedTile implements IFixedTile {

	private static final long serialVersionUID = 4292032135599648285L;
	@Getter
	private final TileType type;
	private final BasicTileProxy proxy;

	private BasicFixedTile(TileType type) {
		this.type = type;
		proxy = new BasicTileProxy(type, true);
	}

	private final static BasicFixedTile[] INSTANCE;
	private final static BasicFixedTile NULL_TILE = new BasicFixedTile(null);

	static {
		INSTANCE = new BasicFixedTile[TileType.values().length];
		for (TileType type : TileType.values())
			INSTANCE[type.ordinal()] = new BasicFixedTile(type);
	}

	public static BasicFixedTile getInstance(TileType type) {
		if (type == null)
			return NULL_TILE;
		return INSTANCE[type.ordinal()];
	}

	private Object writeReplace() throws ObjectStreamException {
		return proxy;
	}

}
