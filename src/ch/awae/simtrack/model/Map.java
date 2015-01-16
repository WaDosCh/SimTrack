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

import java.util.HashMap;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.model.track.BorderTrackTile;

/**
 * Model for the map itself.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public class Map {

	/** The track pieces. */
	private HashMap<TileCoordinate, TrackTile> trackPieces;

	/** The border tracks. */
	private HashMap<TileCoordinate, BorderTrackTile> borderTracks;

	/** The signals. */
	private HashMap<TileEdgeCoordinate, ISignal> signals;

	{
		this.trackPieces = new HashMap<>();
		this.borderTracks = new HashMap<>();
		this.signals = new HashMap<>();
	}

	/**
	 * Gets the track pieces.
	 *
	 * @return the track pieces
	 */
	public HashMap<TileCoordinate, TrackTile> getTrackPieces() {
		return this.trackPieces;
	}

	/**
	 * Sets the track pieces.
	 *
	 * @param trackPieces
	 *            the track pieces
	 */
	public void setTrackPieces(HashMap<TileCoordinate, TrackTile> trackPieces) {
		this.trackPieces = trackPieces;
	}

	/**
	 * Gets the border tracks.
	 *
	 * @return the border tracks
	 */
	public HashMap<TileCoordinate, BorderTrackTile> getBorderTracks() {
		return this.borderTracks;
	}

	/**
	 * Sets the border tracks.
	 *
	 * @param borderTracks
	 *            the border tracks
	 */
	public void setBorderTracks(
			HashMap<TileCoordinate, BorderTrackTile> borderTracks) {
		this.borderTracks = borderTracks;
	}

	/**
	 * Gets the signals.
	 *
	 * @return the signals
	 */
	public HashMap<TileEdgeCoordinate, ISignal> getSignals() {
		return this.signals;
	}

	/**
	 * Sets the signals.
	 *
	 * @param signals
	 *            the signals
	 */
	public void setSignals(HashMap<TileEdgeCoordinate, ISignal> signals) {
		this.signals = signals;
	}

}
