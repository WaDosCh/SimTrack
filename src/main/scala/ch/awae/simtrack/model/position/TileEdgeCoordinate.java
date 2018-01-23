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
 * This class represents the coordinates of an edge of a tile. It extends the
 * {@link TileCoordinate} with a third component indexing the edge. The edges
 * are numbered counter-clockwise with 0 denoting the right edge. Since an is
 * always shared between two tiles and the coordinate representation should not
 * be depending on the tile it was generated from, the coordinate is always
 * based on the tile on the bottom-left of the edge. Therefore only the edge
 * indices 0-2 will be present.
 * 
 * It is also guaranteed that two different instance with the same coordinates
 * will be evaluated as equal and therefore share the same hash code, which is
 * useful for referencing an edge in a hash map without the possession of the
 * original instance
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public class TileEdgeCoordinate extends TileCoordinate {

	private int edge;

	/**
	 * Instantiates a new tile edge coordinate.
	 *
	 * @param u
	 *            the u coordinate
	 * @param v
	 *            the v coordinate
	 * @param edge
	 *            the edge index. This number must be in the inclusive range 0-5
	 */
	public TileEdgeCoordinate(int u, int v, int edge) {
		super(u - ((edge > 2 && edge < 5) ? 1 : 0), v + ((edge > 3) ? 1 : 0));
		assert edge >= 0 && edge < 6;
		this.edge = ((edge > 2) ? (edge - 3) : edge);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileEdgeCoordinate other = (TileEdgeCoordinate) obj;
		if (this.edge != other.edge)
			return false;
		return true;
	}

	/**
	 * Gets the edge index of this coordinate
	 *
	 * @return the edge index
	 */
	public int getEdge() {
		return this.edge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.edge;
		return result;
	}

}
