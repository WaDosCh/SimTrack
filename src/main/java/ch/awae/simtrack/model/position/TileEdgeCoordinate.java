package ch.awae.simtrack.model.position;

public class TileEdgeCoordinate {

	private final TileCoordinate tile;
	private final Edge edge;

	public TileEdgeCoordinate(TileCoordinate tile, Edge edge) {
		this.tile = tile;
		this.edge = edge;
	}

	public TileCoordinate getTile() {
		return tile;
	}

	public Edge getEdge() {
		return edge;
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
