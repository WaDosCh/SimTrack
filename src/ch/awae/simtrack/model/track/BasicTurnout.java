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
package ch.awae.simtrack.model.track;

import ch.awae.simtrack.model.BasicTrackTile;
import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Basic turnout.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
class BasicTurnout extends BasicTrackTile {

	private boolean isLeft = true;

	private int rotation = 0;

	public BasicTurnout(TileCoordinate position) {
		super(position);
		this.rotation = 0;
	}

	@Override
	public ITile cloneTile() {
		BasicTurnout clone = new BasicTurnout(this.getPosition());
		clone.rotation = this.rotation;
		clone.isLeft = this.isLeft;
		return clone;
	}

	@Override
	public float getTravelCost() {
		return 1.2f;
	}

	@Override
	public int[] getRailPaths() {
		return new int[] { this.rotation, (this.rotation + 3) % 6,
				(this.rotation + (this.isLeft ? 1 : -1)) % 6,
				(this.rotation + 3) % 6 };
	}

	@Override
	public void mirror() {
		this.isLeft = !this.isLeft;
	}

	@Override
	public void rotate(boolean isClockwise) {
		this.rotation += isClockwise ? 5 : 1;
		this.rotation %= 6;
	}

}
