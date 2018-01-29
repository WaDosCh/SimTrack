package ch.awae.simtrack.model.position;

import java.io.Serializable;

import lombok.Data;

public @Data class TileEdgeCoordinate implements Serializable {

	private static final long serialVersionUID = -4110990467436553909L;
	public final TileCoordinate tile;
	public final Edge edge;

	public TileEdgeCoordinate(TileCoordinate tile, Edge edge) {
		if (tile == null)
			throw new RuntimeException("tile may not be null");
		this.tile = tile;
		this.edge = edge;
	}

	public TileEdgeCoordinate getOppositeDirection() {
		return new TileEdgeCoordinate(tile.getNeighbour(edge), edge.getOpposite());
	}

	public static TileEdgeCoordinate outbound(TileCoordinate tile, Edge edge) {
		return new TileEdgeCoordinate(tile, edge);
	}

	public static TileEdgeCoordinate inbound(TileCoordinate tile, Edge edge) {
		return outbound(tile, edge).getOppositeDirection();
	}

	public String toString() {
		return "TileEdgeCoordinate[u=" + tile.u + ", v=" + tile.v + ", edge=" + edge + "]";
	}

}
