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
 * This class represents a directed tile edge coordinate. It extends the
 * underlying {@link TileEdgeCoordinate} by additional directional information.
 * Since however the underlying edge coordinate is normalised, the directional
 * information may change depending on the edge index. While the implementation
 * makes sure to compensate for this, any implementation that relies on
 * directional information relative to a specific tile must make sure to account
 * for that. The probably easiest way to cope herewith is to determine the
 * direction by comparison with a reference instance with known direction.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-23
 * @since SimTrack 0.2.1 (0.0.1)
 */
public class DirectedTileEdgeCoordinate extends TileEdgeCoordinate {

	private boolean out;

	/**
	 * Instantiates a new directed tile edge coordinate.
	 *
	 * @param u
	 *            the u component
	 * @param v
	 *            the v component
	 * @param edge
	 *            the edge index
	 * @param out
	 *            indicates whether the coordinate should be directed away from
	 *            the specified tile (u,v) or not.
	 */
	public DirectedTileEdgeCoordinate(int u, int v, int edge, boolean out) {
		super(u, v, edge);
		if (edge < 3)
			this.out = out;
		else
			this.out = !out;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirectedTileEdgeCoordinate other = (DirectedTileEdgeCoordinate) obj;
		if (this.out != other.out)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.out ? 1231 : 1237);
		return result;
	}

	/**
	 * indicates if the coordinate is directed towards the base tile. This
	 * method is semantically equivalent to {@code !isLeaving()}.
	 * 
	 * @return {@code true} if and only if the coordinate is directed towards
	 *         the underlying tile.
	 */
	public boolean isEntering() {
		return !this.out;
	}

	/**
	 * indicates if the coordinate is directed away from the base tile.
	 * 
	 * @return {@code true} if and only if the coordinate is directed away from
	 *         the underlying tile.s
	 */
	public boolean isLeaving() {
		return this.out;
	}

	@Override
	public String toString() {
		return "DirEdge: " + this.getU() + "|" + this.getV() + "; edge "
				+ this.getEdge() + "; out? " + this.out;
	}

	/**
	 * @return the opposite direction
	 * @since 1.2 (SimTrack 0.2.1)
	 **/
	public DirectedTileEdgeCoordinate getOppositeDirection() {
		return new DirectedTileEdgeCoordinate(this.getU(), this.getV(),
				this.getEdge(), !this.out);
	}

}
