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
import java.util.Arrays;
import java.util.List;

import ch.awae.simtrack.model.track.TrackProvider;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-24
 * @since SimTrack 0.2.2
 */
public class TileValidator {

	static List<int[]> validTiles;

	static {
		validTiles = new ArrayList<>();
		for (int i = 0; i < TrackProvider.getTileCount(); i++) {
			ITile t = TrackProvider.getTileInstance(i);
			int[] paths = t.getRailPaths().clone();
			sortPaths(paths);
			validTiles.add(paths);
			if (TrackProvider.isSpecialMirror(i)) {
				t.mirror();
				paths = t.getRailPaths().clone();
				sortPaths(paths);
				validTiles.add(paths);
			}
		}
	}

	public static boolean isValidTrack(ITile tile) {
		int[] paths = tile.getRailPaths().clone();
		sortPaths(paths);
		// STEP 1: check for duplicates
		for (int i = 0; i + 3 < paths.length; i++)
			if (paths[i] == paths[i + 2] && paths[i + 1] == paths[i + 3])
				return false;
		// STEP 2: check for validity
		for (int[] sample : validTiles)
			if (Arrays.equals(sample, paths))
				return true;
		return false;
	}

	private static void sortPaths(int[] list) {
		for (int i = 0; i + 3 < list.length; i += 2)
			for (int j = 0; j + 3 < list.length; j += 2) {
				if (list[j] < list[j + 1]) {
					int temp = list[j];
					list[j] = list[j + 1];
					list[j + 1] = temp;
				}
				if (list[j] > list[j + 2]
						|| (list[j] == list[j + 2] && list[j + 1] > list[j + 3])) {
					int temp = list[j];
					list[j] = list[j + 2];
					list[j + 2] = temp;
					list[j + 1] = list[j + 3];
					list[j + 3] = temp;
				}
			}
	}
}
