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
import ch.awae.simtrack.model.TilePath;
import ch.awae.simtrack.model.TileValidator;

/**
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FusedTrackFactory {

	public static ITile createAnonymousTrack(TilePath[] connections, float cost) {
		return new AnonymousTrack(connections.clone(), cost);
	}

	public static ITile createAnonymousTrack(ITile tile) {
		return new AnonymousTrack(tile.getRailPaths().clone(), tile.getTravelCost());
	}

	public static ITile createFusedTrack(ITile tile0, ITile tile1) {
		TilePath[] con0 = tile0.getRailPaths().clone();
		TilePath[] con1 = tile1.getRailPaths().clone();

		TilePath[] cons = new TilePath[con0.length + con1.length];

		System.arraycopy(con0, 0, cons, 0, con0.length);
		System.arraycopy(con1, 0, cons, con0.length, con1.length);

		cons = clean(cons);

		return new AnonymousTrack(cons, Math.max(tile0.getTravelCost(), tile1.getTravelCost()));
	}

	private static TilePath[] clean(TilePath[] cons) {
		TileValidator.sortPathList(cons);
		ArrayList<TilePath> items = new ArrayList<>();
		for (TilePath p : cons)
			items.add(p);
		for (int i = 0; i + 1 < items.size(); i++) {
			if (items.get(i).equals(items.get(i + 1))) {
				items.remove(i + 1);
				i--;
			}
		}
		return items.toArray(new TilePath[0]);
	}
}
