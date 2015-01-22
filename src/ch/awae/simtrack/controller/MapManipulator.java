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

import static ch.awae.simtrack.HighLogic.map;

import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.GraphFactory;

/**
 * Map Manipulator
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class MapManipulator {

	public static boolean canPlaceOn(TileCoordinate c) {
		assert c != null;
		return !(map.getBorderTracks().containsKey(c) || map.getTrackPieces()
				.containsKey(c));
	}

	public static boolean canRemoveFrom(TileCoordinate c) {
		assert c != null;
		return map.getTrackPieces().containsKey(c);
	}

	private static void forceUpdates() {
		map.graph = GraphFactory.buildGraph(map);
		BlockBuilder.updateBlocks();
		// TODO: notify pathfinders
	}

	public static void place(TrackTile t) {
		if (!canPlaceOn(t.getPosition()))
			return;
		map.getTrackPieces().put(t.getPosition(), t);
		forceUpdates();
	}

	public static TrackTile remove(TileCoordinate c) {
		TrackTile t = map.getTrackPieces().remove(c);
		forceUpdates();
		return t;
	}
}
