package ch.awae.simtrack.scene.game.model.tile.track;

import java.util.ArrayList;

import javax.json.JsonObject;

import ch.awae.simtrack.scene.game.model.tile.TransformableTrackTile;
import ch.awae.simtrack.util.Resource;

/**
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-26
 * @since SimTrack 0.2.2 (0.2.1)
 */
public class TrackProvider {

	private static ArrayList<TransformableTrackTile> tiles;

	static {
		tiles = new ArrayList<>();

		JsonObject tracks = Resource.getJSON("tracks.json");

		for (String key : tracks.keySet()) {
			tiles.add(new MutableTrack(tracks.getJsonObject(key)));
		}
	}

	public static TransformableTrackTile getTileInstance(int tileID) {
		return tiles.get(tileID);
	}

	public static int getTileCount() {
		return tiles.size();
	}

}