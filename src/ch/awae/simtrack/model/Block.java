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
package ch.awae.simtrack.model;

import java.util.ArrayList;

import ch.awae.simtrack.HighLogic;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Signal Block
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.1.1
 */
public class Block {

	private ArrayList<TileCoordinate> members;
	private final int id;

	public Block(int id) {
		this.members = new ArrayList<>();
		this.id = id;
	}

	public void addTile(TileCoordinate t) {
		if (!this.members.contains(t))
			this.members.add(t);
	}

	public void merge(Block other) {
		for (TileCoordinate t : other.members) {
			this.addTile(t);
			HighLogic.map.getTileAt(t).setBlock(this);
		}
	}

	public int getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		return (this.id == ((Block) obj).id);
	}

}
