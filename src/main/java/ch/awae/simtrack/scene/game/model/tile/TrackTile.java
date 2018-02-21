package ch.awae.simtrack.scene.game.model.tile;

import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TilePath;

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
