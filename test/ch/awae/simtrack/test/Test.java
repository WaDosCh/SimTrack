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
import java.util.HashMap;

import ch.awae.simtrack.model.ISignal;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.model.track.CurvedRail;
import ch.awae.simtrack.view.Graph;
import ch.awae.simtrack.view.GraphFactory;

@SuppressWarnings("javadoc")
public class Test {

	public static void main(String[] args) {

		CurvedRail a = new CurvedRail(new TileCoordinate(2, 0));
		a.rotate(true);
		a.rotate(true);
		a.rotate(true);
		CurvedRail b = new CurvedRail(new TileCoordinate(2, 1));
		b.rotate(false);
		b.rotate(false);
		CurvedRail c = new CurvedRail(new TileCoordinate(1, 2));
		c.rotate(false);
		CurvedRail d = new CurvedRail(new TileCoordinate(0, 2));
		CurvedRail e = new CurvedRail(new TileCoordinate(0, 1));
		e.rotate(true);
		CurvedRail f = new CurvedRail(new TileCoordinate(1, 0));
		f.rotate(true);
		f.rotate(true);

		ArrayList<TrackTile> tracks = new ArrayList<>();
		tracks.add(a);
		tracks.add(b);
		tracks.add(c);
		tracks.add(d);
		tracks.add(e);
		tracks.add(f);

		Map m = new Map();

		HashMap<TileCoordinate, TrackTile> trackPieces = m.getTrackPieces();

		tracks.forEach(track -> trackPieces.put(track.getPosition(), track));

		DirectedTileEdgeCoordinate sigCoor = new DirectedTileEdgeCoordinate(1,
				0, 0, true);

		m.getSignals().put(sigCoor, new ISignal() {

			@Override
			public void setPosition(TileEdgeCoordinate position) {
				// test
			}

			@Override
			public TileEdgeCoordinate getPosition() {
				return sigCoor;
			}

			@Override
			public boolean blocksOutward() {
				return true;
			}

			@Override
			public boolean blocksInward() {
				return false;
			}
		});

		long t1 = System.currentTimeMillis();

		Graph graph = GraphFactory.buildGraph(m);

		long t2 = System.currentTimeMillis();

		System.out.println(t2 - t1);

		HashMap<DirectedTileEdgeCoordinate, HashMap<DirectedTileEdgeCoordinate, Double>> map = graph
				.getMap();

		map.forEach((pos, subM) -> {
			System.out.println(pos);
			subM.forEach((p, co) -> System.out.println(" > " + p + " (" + co
					+ ")"));
		});

	}
}
