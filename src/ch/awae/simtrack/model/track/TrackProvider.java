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

import java.util.ArrayList;

import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-26
 * @since SimTrack 0.2.2 (0.2.1)
 */
public class TrackProvider {

	private static ArrayList<ITile> tiles;

	static {
		tiles = new ArrayList<>();
		// register tiles
		tiles.add(new StraightRail(new TileCoordinate(0, 0)));
		tiles.add(new CurvedRail(new TileCoordinate(0, 0)));
		tiles.add(new StraightCrossing(new TileCoordinate(0, 0)));
		tiles.add(new StraightCurvedCrossing(new TileCoordinate(0, 0)));
		tiles.add(new CurvedCrossing(new TileCoordinate(0, 0)));
		tiles.add(new BasicTurnout(new TileCoordinate(0, 0)));
		tiles.add(new ThreeWayTurnout(new TileCoordinate(0, 0)));
		tiles.add(new WyeSwitch(new TileCoordinate(0, 0)));
		tiles.add(new SingleSlip(new TileCoordinate(0, 0)));
		tiles.add(new DoubleSlip(new TileCoordinate(0, 0)));
	}

	public static ITile getTileInstance(int tileID) {
		return tiles.get(tileID).cloneTile();
	}

	public static int getTileCount() {
		return tiles.size();
	}

}
