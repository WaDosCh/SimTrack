package ch.awae.simtrack.model;

import lombok.Getter;

/**
 * Class managing basic tiles.
 * 
 * A basic tile is a simple tile instance fully defined by its type. For each
 * TileType a single instance is provided.
 * 
 * @see #getInstance(TileType)
 */
public final class BasicTile implements ITile {

	private static final long serialVersionUID = 6343687845845067422L;
	@Getter
	private final TileType type;

	private BasicTile(TileType type) {
		this.type = type;
	}

	private final static BasicTile[] INSTANCE;
	private final static BasicTile NULL_TILE = new BasicTile(null);

	static {
		INSTANCE = new BasicTile[TileType.values().length];
		for (TileType type : TileType.values())
			INSTANCE[type.ordinal()] = new BasicTile(type);
	}

	public static BasicTile getInstance(TileType type) {
		if (type == null)
			return NULL_TILE;
		return INSTANCE[type.ordinal()];
	}

}
