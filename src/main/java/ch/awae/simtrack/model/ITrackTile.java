package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.Edge;

public interface ITrackTile extends ITile {

	public float getTravelCost();

	public boolean connectsAt(Edge edge);

	public TilePath[] getRailPaths();
	
	@Override
	default TileType getType() {
		return TileType.TRACK;
	}

}
