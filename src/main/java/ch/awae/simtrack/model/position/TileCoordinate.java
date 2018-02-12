package ch.awae.simtrack.model.position;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

/**
 * This defines a integer coordinate on the hexagonal tiles of the game model
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
@EqualsAndHashCode
public class TileCoordinate implements Serializable {

	private static final long serialVersionUID = -1177199223722620095L;

	/**
	 * exact value of the sine of 60 degrees (pi/3)
	 */
	private static final double SQRT3DIV2 = Math.sqrt(3) / 2;

	public final int u, v;

	/**
	 * Instantiates a new tile coordinate.
	 *
	 * @param u
	 *            the u component of the coordinate. This corresponds to the
	 *            horizontal axis in a laying grid
	 * @param v
	 *            the v component of the coordinate. This corresponds to the
	 *            "top-left-to-bottom-right" diagonal axis in a laying grid
	 */
	public TileCoordinate(int u, int v) {
		this.u = u;
		this.v = v;
	}

	/**
	 * calculates the distance between two tiles. This is the euclidian distance
	 * between the centres of the two tiles. This value will always be smaller
	 * than the minimum step count when travelling the hex grid. Therefore this
	 * value can be used as an inadmissible heuristic for basic pathfinding
	 *
	 * @param other
	 *            the coordinate to calculate the distance to
	 * @return the euclidian distance to the other tile coordinate
	 */
	public double distanceTo(TileCoordinate other) {
		double x = (this.u - other.u) + (this.v - other.v) * 0.5;
		double y = (this.v - other.v) * SQRT3DIV2;

		return Math.sqrt(x * x + y * y);
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

	@Deprecated
	public SceneCoordinate getSceneCoordinate() {
		return toSceneCoordinate();
	}
	
	/**
	 * calculates the scene pixel coordinates of the centre of a given hex tile.
	 * The scene coordinates are not the screen coordinates.
	 * 
	 * @return the position of the hex centre on the scene.
	 */
	public SceneCoordinate toSceneCoordinate() {
		double x = (2 * this.u + this.v) * 50;
		double y = this.v * Math.sqrt(3) * 50;
		return new SceneCoordinate(x, y);
	}

}
