package ch.awae.simtrack.model.tile;

import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TilePath;

public interface TrackTile extends Tile {

	public float getTravelCost();

	public boolean connectsAt(Edge edge);

	public TilePath[] getRailPaths();

	@Override
	default TileType getType() {
		return TileType.TRACK;
	}

	/**
	 * Provides all (directed) paths within the tile
	 * 
	 * @param origin
	 * @return
	 */
	public TilePath[] getPaths();

}
