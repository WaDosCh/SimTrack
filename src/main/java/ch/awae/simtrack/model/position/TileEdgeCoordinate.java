package ch.awae.simtrack.model.position;

import lombok.Data;

public @Data class TileEdgeCoordinate {

	public final TileCoordinate tile;
	public final Edge edge;

	public TileEdgeCoordinate getOppositeDirection() {
		return new TileEdgeCoordinate(tile.getNeighbour(edge),
				edge.getOpposite());
	}

	public static TileEdgeCoordinate outbound(TileCoordinate tile, Edge edge) {
		return new TileEdgeCoordinate(tile, edge);
	}

	public static TileEdgeCoordinate inbound(TileCoordinate tile, Edge edge) {
		return outbound(tile, edge).getOppositeDirection();
	}

	public String toString() {
		return "TileEdgeCoordinate[u=" + tile.u + ", v=" + tile.v + ", edge="
				+ edge + "]";
	}

}
