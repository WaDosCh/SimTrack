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
package ch.awae.simtrack.controller;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.Graph;
import ch.awae.simtrack.view.GraphFactory;

/**
 * Map Manipulator
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class MapManipulator {

	private static final MapManipulator INSTANCE = new MapManipulator();

	public static MapManipulator instance() {
		return INSTANCE;
	}

	// =========================================

	private static void forceUpdates() {
		@SuppressWarnings("unused")
		Graph g = GraphFactory.buildGraph(Global.map);
		// TODO: propagate graph update to path-finders
	}

	public boolean canRemoveFrom(TileCoordinate c) {
		assert c != null;
		return Global.map.getTrackPieces().containsKey(c);
	}

	public boolean canPlaceOn(TileCoordinate c) {
		assert c != null;
		Map m = Global.map;
		return !(m.getBorderTracks().containsKey(c) || m.getTrackPieces()
				.containsKey(c));
	}

	public void place(TrackTile t) {
		if (!this.canPlaceOn(t.getPosition()))
			return;
		Global.map.getTrackPieces().put(t.getPosition(), t);
		forceUpdates();
	}

	public TrackTile remove(TileCoordinate c) {
		TrackTile t = Global.map.getTrackPieces().remove(c);
		forceUpdates();
		return t;
	}
}
