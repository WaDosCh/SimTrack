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

import java.util.ArrayList;

import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.track.BorderTrackTile;

/**
 * This interface describes the utility that spawns the border connections when
 * creating a new game
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public interface IBorderConnectionSpawner {

	/**
	 * spawns border connections for the map.
	 * 
	 * @param map
	 *            the map to spawn for
	 * @return the border connections. Duplicate positions are allowed but will
	 *         be ignored.
	 */
	public ArrayList<BorderTrackTile> spawnConnections(Map map);
}
