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

import ch.awae.simtrack.model.RotatableTile;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.renderer.TrackRenderUtil;

/**
 * Implementation for a double slip
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class DoubleSlip extends TrackTile implements RotatableTile {

	public DoubleSlip(TileCoordinate position) {
		super(position);
		this.rotation = 0;
	}

	private int rotation;

	@Override
	public void rotate(boolean isClockwise) {
		this.rotation += isClockwise ? 2 : 1;
		this.rotation %= 3;
	}

	@Override
	public void mirror() {
		// stays static under mirroring
	}

	@Override
	public float[][] getRawPaths() {
		return new float[][] { { this.rotation, this.rotation + 3, 1 },
				{ this.rotation + 1, (this.rotation + 4) % 6, 1 },
				{ this.rotation, (this.rotation + 4) % 6, 1 },
				{ this.rotation + 1, this.rotation + 3, 1 } };
	}

	@Override
	public void renderBed(Graphics2D g) {
		g.rotate(-Math.PI / 3 * this.rotation);
		TrackRenderUtil.renderStraightRailbed(g, 8, 5, 45);
		g.rotate(-Math.PI / 3);
		TrackRenderUtil.renderStraightRailbed(g, 8, 5, 45);
		TrackRenderUtil.renderCurvedRailbed(g, 8, 5, 45);
		g.rotate(Math.PI);
		TrackRenderUtil.renderCurvedRailbed(g, 8, 5, 45);
	}

	@Override
	public void renderRail(Graphics2D g) {
		g.rotate(-Math.PI / 3 * this.rotation);
		TrackRenderUtil.renderStraightRail(g, 30);
		g.rotate(-Math.PI / 3);
		TrackRenderUtil.renderStraightRail(g, 30);
		TrackRenderUtil.renderCurvedRail(g, 30);
		g.rotate(Math.PI);
		TrackRenderUtil.renderCurvedRail(g, 30);
	}

	@Override
	public TrackTile cloneTrack() {
		DoubleSlip clone = new DoubleSlip(this.getPosition());
		clone.rotation = this.rotation;
		return clone;
	}

}
