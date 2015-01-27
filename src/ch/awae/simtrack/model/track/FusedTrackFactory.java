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
import ch.awae.simtrack.model.TileValidator;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FusedTrackFactory {

	public static ITile createAnonymousTrack(TileCoordinate position,
			int[] connections, float cost) {
		return new AnonymousTrack(position, connections.clone(), cost);
	}

	public static ITile createAnonymousTrack(ITile tile) {
		return new AnonymousTrack(tile.getPosition(), tile.getRailPaths()
				.clone(), tile.getTravelCost());
	}

	public static ITile createFusedTrack(ITile tile0, ITile tile1) {
		int[] con0 = tile0.getRailPaths().clone();
		int[] con1 = tile1.getRailPaths().clone();

		int[] cons = new int[con0.length + con1.length];

		System.arraycopy(con0, 0, cons, 0, con0.length);
		System.arraycopy(con1, 0, cons, con0.length, con1.length);

		cons = clean(cons);

		return new AnonymousTrack(tile0.getPosition(), cons, Math.max(
				tile0.getTravelCost(), tile1.getTravelCost()));
	}

	private static int[] clean(int[] cons) {
		TileValidator.sortPathList(cons);
		ArrayList<Integer> items = new ArrayList<>();
		for (int i : cons)
			items.add(new Integer(i));

		for (int i = 0; i + 3 < items.size(); i += 2) {
			if (items.get(i).equals(items.get(i + 2))
					&& items.get(i + 1).equals(items.get(i + 3))) {
				items.remove(i + 3);
				items.remove(i + 2);
				i -= 2;
			}
		}

		int[] res = new int[items.size()];

		for (int i = 0; i < items.size(); i++)
			res[i] = items.get(i).intValue();

		return res;
	}
}
