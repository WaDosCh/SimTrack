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
package ch.awae.simtrack.test;

import java.util.ArrayList;

import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.StraightRail;

@SuppressWarnings("javadoc")
public class Test {

	public static void main(String[] args) {

		TrackTile a = new StraightRail(new TileCoordinate(0, 0));
		TrackTile b = new StraightRail(new TileCoordinate(1, 0));

		ArrayList<TrackTile> tracks = new ArrayList<>();
		tracks.add(a);
		tracks.add(b);

		/*
		 * Graph graph = GraphFactory.buildGraph(tracks);
		 * 
		 * graph.getNeighbours(new DirectedTileEdgeCoordinate(1, 0, 0, false))
		 * .forEach(e -> System.out.println(e.getKey()));
		 */
	}
}
