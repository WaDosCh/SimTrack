package ch.awae.simtrack.scene.game.model.tile.track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.util.Resource;
import lombok.Getter;

public class TrackProvider {

	private static @Getter List<ConstructionTrackTile> tiles;

	static {
		tiles = new ArrayList<>();
		JsonObject tracks = Resource.getJSON("tracks.json");
		for (String key : tracks.keySet()) {
			tiles.add(getTrackFromJson(tracks.getJsonObject(key)));
		}
		tiles = Collections.unmodifiableList(tiles);
	}

	private static ConstructionTrackTile getTrackFromJson(JsonObject json) {
		JsonArray a = json.getJsonArray("links");
		int buildCost = json.getInt("buildCost");
		boolean showInToolbar = true;
		if (json.containsKey("showInToolbar"))
			showInToolbar = json.getBoolean("showInToolbar");
		TilePath[] paths = new TilePath[a.size() / 2];
		for (int i = 0; i + 1 < a.size(); i += 2) {
			paths[i / 2] = new TilePath(Edge.byOrdinal(a.getInt(i)), Edge.byOrdinal(a.getInt(i + 1)));
		}
		// first edge becomes base edge automatically
		Edge baseEdge = Edge.byOrdinal(a.getInt(0));
		return new ConstructionTrackTile(paths, baseEdge, buildCost, showInToolbar);
	}

}
