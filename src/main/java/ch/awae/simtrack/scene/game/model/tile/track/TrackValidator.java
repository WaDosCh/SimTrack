package ch.awae.simtrack.scene.game.model.tile.track;

import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Validation class to determine whether or not a tile is valid
 */
public class TrackValidator {

	private static Logger logger = LogManager.getLogger();
	private static HashSet<TrackTile> validTiles;
	private static HashSet<ConstructionTrackTile> properConstructionTiles;

	static {
		validTiles = new HashSet<>();
		properConstructionTiles = new HashSet<>();
		for (ConstructionTrackTile t0 : TrackProvider.getTiles()) {
			ConstructionTrackTile t1 = t0.mirrored();
			for (int r = 0; r < 6; r++) {
				validTiles.add(t0.getNormalTrackTile());
				validTiles.add(t1.getNormalTrackTile());
				properConstructionTiles.add(t0);
				properConstructionTiles.add(t1);
				t0 = t0.rotated(false);
				t1 = t1.rotated(false);
			}
		}
	}

	/**
	 * @return searches for the properly created construction tile with correct buildCost and details. This may be
	 *         useful when fusing track tiles and then checking for actual buildCost values.
	 */
	public static ConstructionTrackTile getProperTileFor(TrackTile tile) {
		for (ConstructionTrackTile atile : properConstructionTiles) {
			if (atile.equals(tile))
				return atile;
		}
		return null;
	}

	/**
	 * checks if a tile is valid
	 * 
	 * @param tile
	 * @return {@code true} if and only if the provided tile is valid.
	 */
	public static boolean isValidTrack(TrackTile tile) {
		if (tile instanceof ConstructionTrackTile) {
			return validTiles.contains(((ConstructionTrackTile) tile).getNormalTrackTile());
		}
		return validTiles.contains(tile);
	}

}
