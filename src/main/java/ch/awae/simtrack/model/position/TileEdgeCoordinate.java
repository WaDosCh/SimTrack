package ch.awae.simtrack.model.position;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TileEdgeCoordinate {

	public final TileCoordinate tile;
	public final Edge edge;

	public TileEdgeCoordinate(TileCoordinate tile, Edge edge) {
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

}
