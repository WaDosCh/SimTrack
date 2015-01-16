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
import java.util.HashMap;

import controller.IBorderConnectionSpawner;
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

	/**
	 * Creates a new board instance
	 * 
	 * @param hSize
	 * @param vSize
	 * @param conSpawn
	 */
	public Map(int hSize, int vSize, IBorderConnectionSpawner conSpawn) {
		assert hSize > 0 && vSize > 0;
		this.hSize = hSize;
		this.vSize = vSize;
		ArrayList<BorderTrackTile> cons = conSpawn.spawnConnections(this);
		cons.forEach(con -> this.borderTracks.put(con.getPosition(), con));
	}

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
	 * Gets the border tracks.
	 *
	 * @return the border tracks
	 */
	public HashMap<TileCoordinate, BorderTrackTile> getBorderTracks() {
		return this.borderTracks;
	}

	/**
	 * Gets the signals.
	 *
	 * @return the signals
	 */
	public HashMap<TileEdgeCoordinate, ISignal> getSignals() {
		return this.signals;
	}

	private final int hSize, vSize;

	/**
	 * returns the horizontal tile count.
	 * 
	 * @return the horizontal board size
	 */
	public int getHorizontalSize() {
		return this.hSize;
	}

	/**
	 * returns the vertical tile count.
	 * 
	 * @return the vertical board size
	 */
	public int getVerticalSize() {
		return this.vSize;
	}

}
