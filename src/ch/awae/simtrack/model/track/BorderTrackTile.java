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

import java.awt.Graphics2D;

import ch.awae.simtrack.model.BorderConnection;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Implementation for the border track pieces. They do not contain any paths.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class BorderTrackTile extends TrackTile implements BorderConnection {

	private Direction direction;

	private int edge;

	public BorderTrackTile(TileCoordinate position, int edge, boolean isOutput) {
		super(position);
		assert edge >= 0 && edge < 6;
		this.edge = edge;
		this.direction = isOutput ? Direction.OUT : Direction.IN;
	}

	@Override
	public TrackTile cloneTrack() {
		return null;
	}

	@Override
	public Direction getDirection() {
		return this.direction;
	}

	public int getEdge() {
		return this.edge;
	}

	// IRRELEVANT INTERFACE METHODS

	@Override
	public DirectedTileEdgeCoordinate getInterfacingEdge() {
		TileCoordinate pos = this.getPosition();
		return new DirectedTileEdgeCoordinate(pos.getU(), pos.getV(),
				this.edge, this.direction != Direction.OUT);
	}

	@Override
	public float[][] getRawPaths() {
		return null;
	}

	@Override
	public void renderBed(Graphics2D g) {
		return;
	}

	@Override
	public void renderRail(Graphics2D g) {
		return;

	}

}
