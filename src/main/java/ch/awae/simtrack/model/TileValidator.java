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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.awae.simtrack.model.track.TrackProvider;

/**
 * Validation class to determine whether or not a tile is valid
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class TileValidator {

	private static List<TilePath[]> validTiles;
	private static Map<Integer, ITrackTile> tileCache;

	static {
		validTiles = new ArrayList<>();
		tileCache = new HashMap<>();
		for (int i = 0; i < TrackProvider.getTileCount(); i++) {
			TilePath[] paths;
			ITransformableTrackTile t0 = TrackProvider.getTileInstance(i);
			ITransformableTrackTile t1 = t0.mirrored();
			for (int r = 0; r < 6; r++) {
				paths = t0.getRailPaths().clone();
				sortPathList(paths);
				addIfNotThere(paths);
				paths = t1.getRailPaths().clone();
				sortPathList(paths);
				addIfNotThere(paths);
				t0 = t0.rotated(false);
				t1 = t1.rotated(false);
			}
		}

	}

	private static void addIfNotThere(TilePath[] item) {
		for (TilePath[] entry : validTiles)
			if (Arrays.equals(entry, item))
				return;
		validTiles.add(item);
	}

	/**
	 * checks if a tile is valid
	 * 
	 * @param tile
	 * @return {@code true} if and only if the provided tile is valid.
	 */
	public static boolean isValidTrack(ITrackTile tile) {
		TilePath[] paths = tile.getRailPaths().clone();
		sortPathList(paths);

		// STEP 1: check for duplicates
		for (int i = 0; i + 1 < paths.length; i += 2)
			if (paths[i].equals(paths[i + 1]))
				return false;
		// STEP 2: check for validity
		for (TilePath[] sample : validTiles)
			if (Arrays.equals(sample, paths))
				return true;
		return false;
	}

	public static void sortPathList(TilePath[] list) {
		for (int i = 0; i < list.length; i++)
			list[i] = list[i].normalised();
		Arrays.sort(list);
		return;
	}

	/**
	 * Caches the tile if it is the first of it's kind to be placed, returns the
	 * cached version otherwise
	 * 
	 * @return
	 */
	public static ITrackTile intern(ITrackTile tile) {
		Integer key = Integer.valueOf(Arrays.hashCode(tile.getRailPaths()));
		ITrackTile res = tileCache.get(key);
		if (res == null) {
			tileCache.put(key, tile);
			System.out.println("added new tile to cache with hash: " + key);
			res = tile;
		}
		return res;
	}
}
