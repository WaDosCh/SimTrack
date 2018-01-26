package ch.awae.simtrack.model;

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

	@Getter
	private final TileType type;

	private BasicFixedTile(TileType type) {
		this.type = type;
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

}
