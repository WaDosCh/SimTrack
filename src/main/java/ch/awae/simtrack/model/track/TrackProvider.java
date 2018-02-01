package ch.awae.simtrack.model.track;

import java.util.ArrayList;

import javax.json.JsonObject;

import ch.awae.simtrack.model.tile.ITransformableTrackTile;
import ch.awae.simtrack.util.Resource;

/**
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-26
 * @since SimTrack 0.2.2 (0.2.1)
 */
public class TrackProvider {

	private static ArrayList<ITransformableTrackTile> tiles;

	static {
		tiles = new ArrayList<>();

		JsonObject tracks = Resource.getJSON("tracks.json");

		for (String key : tracks.keySet()) {
			tiles.add(new MutableTrack(tracks.getJsonObject(key)));
		}
	}

	public static ITransformableTrackTile getTileInstance(int tileID) {
		return tiles.get(tileID);
	}

	public static int getTileCount() {
		return tiles.size();
	}

}
