package ch.awae.simtrack.controller;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.Graph;
import ch.awae.simtrack.view.GraphFactory;

public class TilePlacer {

	private static final TilePlacer INSTANCE = new TilePlacer();

	public static TilePlacer instance() {
		return INSTANCE;
	}

	// =========================================

	private static void forceUpdates() {
		@SuppressWarnings("unused")
		Graph g = GraphFactory.buildGraph(Global.map);
		// TODO: propagate graph update to path-finders
	}

	public boolean canRemoveFrom(TileCoordinate c) {
		assert c != null;
		return Global.map.getTrackPieces().containsKey(c);
	}

	public boolean canPlaceOn(TileCoordinate c) {
		assert c != null;
		Map m = Global.map;
		return !(m.getBorderTracks().containsKey(c) || m.getTrackPieces()
				.containsKey(c));
	}

	public void place(TrackTile t) {
		if (!this.canPlaceOn(t.getPosition()))
			return;
		Global.map.getTrackPieces().put(t.getPosition(), t);
		forceUpdates();
	}

	public TrackTile remove(TileCoordinate c) {
		TrackTile t = Global.map.getTrackPieces().remove(c);
		forceUpdates();
		return t;
	}
}
