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
import java.util.Random;

import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.BorderTrackTile;

/**
 * This basic implementation spawns connections on the edges with a random
 * distribution.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-16
 * @since SimTrack 0.0.1
 */
public class BasicBorderConnectionSpawner implements IBorderConnectionSpawner {

	private int connectionCount;

	private Random random;
	/**
	 * creates a new spawner instance. The same instance can be re-used.
	 * 
	 * @param connectionCount
	 *            the number of connections the spawner should place.
	 */
	public BasicBorderConnectionSpawner(int connectionCount) {
		this.connectionCount = connectionCount;
		this.random = new Random();
	}

	@Override
	public ArrayList<BorderTrackTile> spawnConnections(Map map) {

		ArrayList<BorderTrackTile> list = new ArrayList<>();

		for (int i = 0; i < this.connectionCount; i++) {

			final int ho = map.getHorizontalSize();
			final int ve = map.getVerticalSize();

			boolean isHo = this.random.nextInt(ho + ve / 2) < ho;
			boolean isTo = this.random.nextBoolean();

			int r1 = this.random.nextInt(isHo ? ho : ((ve + 1) / 2));

			int edge, u, v;
			// STEP 2: determine position / orient
			if (isHo) {
				v = isTo ? 0 : ve - 1;
				u = r1 - v / 2;
				if (r1 == 0)
					// left corners
					edge = isTo ? 5 : 1;
				else if (r1 == ho - 1)
					// right corners
					edge = isTo ? 4 : 2;
				else if (isTo)
					// top edge
					edge = this.random.nextBoolean() ? 4 : 5;
				else
					// bottom edge
					edge = this.random.nextBoolean() ? 1 : 2;
			} else {
				r1 += 2;
				r1 *= 2;
				v = r1;
				if (r1 + 1 >= ve)
					continue;
				u = (isTo ? 0 : ho - 1) - (v / 2);
				edge = this.random.nextInt(3)
						+ (this.random.nextBoolean() ? 5 : 2);
				if (edge > 5)
					edge -= 6;
			}

			// STEP 3: assemble
			BorderTrackTile tile = new BorderTrackTile(
					new TileCoordinate(u, v), edge, this.random.nextBoolean());

			list.add(tile);
		}
		return list;
	}
}
