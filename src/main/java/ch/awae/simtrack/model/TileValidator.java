package ch.awae.simtrack.model;

import java.util.*;

import ch.awae.simtrack.controller.Log;
import ch.awae.simtrack.model.position.TilePath;
import ch.awae.simtrack.model.tile.ITrackTile;
import ch.awae.simtrack.model.tile.ITransformableTrackTile;
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
			Log.info("added new tile to cache with hash: " + key);
			res = tile;
		}
		return res;
	}
}
