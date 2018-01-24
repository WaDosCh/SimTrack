/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.model.position;

/**
 * This class defines a hexadecimal coordinate
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public class TileCoordinate {

	/*
	 * exact value of the sine of 60 degrees (pi/3)
	 */
	private static final double SQRT3DIV2 = Math.sqrt(3) / 2;

	private final int u, v;

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileCoordinate other = (TileCoordinate) obj;
		if (this.u != other.u)
			return false;
		if (this.v != other.v)
			return false;
		return true;
	}

	/**
	 * Gets the u component
	 *
	 * @return the u component
	 */
	public int getU() {
		return this.u;
	}

	/**
	 * Gets the v component
	 *
	 * @return the v component
	 */
	public int getV() {
		return this.v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.u;
		result = prime * result + this.v;
		return result;
	}

	@Override
	public String toString() {
		return "Hex[" + this.u + "," + this.v + "]";
	}

	public TileEdgeCoordinate getEdge(Edge edge) {
		return new TileEdgeCoordinate(this, edge);
	}

	public TileCoordinate getNeighbour(Edge edge) {
		return new TileCoordinate(u + edge.Δu, v + edge.Δv);
	}

}
