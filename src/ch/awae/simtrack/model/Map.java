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

import ch.awae.simtrack.controller.IBorderConnectionSpawner;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.model.track.BorderTrackTile;
import ch.awae.simtrack.view.Graph;

/**
 * Model for the map itself.
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Map {

	/** @since 1.3 **/
	public Graph graph = null;
	/** @since 1.3 **/
	public ArrayList<Block> blocks = null;

	/** The border tracks. */
	private HashMap<TileCoordinate, BorderTrackTile> borderTracks;

	private final int hSize, vSize;

	/** The signals. */
	private HashMap<TileEdgeCoordinate, ISignal> signals;

	/** The track pieces. */
	private HashMap<TileCoordinate, TrackTile> trackPieces;

	{
		this.trackPieces = new HashMap<>();
		this.borderTracks = new HashMap<>();
		this.signals = new HashMap<>();
	}

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
		if (cons != null)
			cons.forEach(con -> this.borderTracks.put(con.getPosition(), con));
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
	 * returns the horizontal tile count.
	 * 
	 * @return the horizontal board size
	 */
	public int getHorizontalSize() {
		return this.hSize;
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
	 * Gets the track pieces.
	 *
	 * @return the track pieces
	 */
	public HashMap<TileCoordinate, TrackTile> getTrackPieces() {
		return this.trackPieces;
	}

	/**
	 * returns the vertical tile count.
	 * 
	 * @return the vertical board size
	 */
	public int getVerticalSize() {
		return this.vSize;
	}

	/**
	 * retrieves the signal at the given position
	 * 
	 * @param t
	 *            the position to retrieve for
	 * @return the signal at the given position
	 * @since 1.3 (SimTrack 0.1.1)
	 */
	public ISignal getSignalAt(TileEdgeCoordinate t) {
		return this.signals.getOrDefault(t, null);
	}

	public TrackTile getTileAt(TileCoordinate t) {
		return this.trackPieces.getOrDefault(t, this.borderTracks.get(t));
	}

}
