package ch.awae.simtrack.scene.game.model.tile.track;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.util.Resource;

public class TrackProvider {

	private static List<ConstructionTrackTile> tiles;

	static {
		tiles = new ArrayList<>();

		JsonObject tracks = Resource.getJSON("tracks.json");

		for (String key : tracks.keySet()) {
			tiles.add(getTrackFromJson(tracks.getJsonObject(key)));
		}
	}

	public static ConstructionTrackTile getTileInstance(int tileID) {
		return tiles.get(tileID);
	}

	public static int getTileCount() {
		return tiles.size();
	}

	private static ConstructionTrackTile getTrackFromJson(JsonObject json) {
		JsonArray a = json.getJsonArray("links");
		int buildCost = json.getInt("buildCost");
		TilePath[] paths = new TilePath[a.size() / 2];
		for (int i = 0; i + 1 < a.size(); i += 2) {
			paths[i / 2] = new TilePath(Edge.byIndex(a.getInt(i)), Edge.byIndex(a.getInt(i + 1)));
		}
		// first edge becomes base edge automatically
		Edge baseEdge = Edge.byIndex(a.getInt(0));
		return new ConstructionTrackTile(paths, baseEdge, buildCost);
	}

}
