package ch.awae.simtrack.scene.game.model.position;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

/**
 * This defines a integer coordinate on the hexagonal tiles of the game model
 */
@EqualsAndHashCode
public class TileCoordinate implements Serializable {

	private static final long serialVersionUID = -1177199223722620095L;

	/**
	 * also known as U/2 in our notes
	 */
	public static final int TILE_SIDE_HEIGHT_HALF = 29;

	/**
	 * the s offset between two tiles positioned at (u,v) and (u,v+1)
	 */
	public static final int TILE_V_S_OFFSET = 50;

	/**
	 * the s offset between two tiles positioned at (u,v) and (u+1,v)
	 */
	public static final int TILE_U_S_OFFSET = 2 * TILE_V_S_OFFSET;

	/**
	 * the t offset between two tiles positioned at (u,v) and (u,v+1)
	 */
	public static final int TILE_V_T_OFFSET = 3 * TILE_SIDE_HEIGHT_HALF; // 87

	public final int u, v;

	/**
	 * Instantiates a new tile coordinate.
	 *
	 * @param u the u component of the coordinate. This corresponds to the horizontal axis in a laying grid
	 * @param v the v component of the coordinate. This corresponds to the "top-left-to-bottom-right" diagonal axis in a
	 *            laying grid
	 */
	public TileCoordinate(int u, int v) {
		this.u = u;
		this.v = v;
	}

	@Override
	public String toString() {
		return "Tile[u=" + this.u + ", v=" + this.v + "]";
	}

	public TileEdgeCoordinate getEdge(Edge edge) {
		return new TileEdgeCoordinate(this, edge);
	}

	public TileCoordinate getNeighbour(Edge edge) {
		return new TileCoordinate(u + edge.Δu, v + edge.Δv);
	}

	/**
	 * calculates the scene pixel coordinates of the centre of a given hex tile. The scene coordinates are not the
	 * screen coordinates.
	 * 
	 * @return the position of the hex centre on the scene.
	 */
	public SceneCoordinate toSceneCoordinate() {
		double s = (2 * this.u + this.v) * TILE_V_S_OFFSET;
		double t = this.v * TILE_V_T_OFFSET;
		return new SceneCoordinate(s, t);
	}

}
