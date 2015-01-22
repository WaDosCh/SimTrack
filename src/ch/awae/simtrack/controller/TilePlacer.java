package ch.awae.simtrack.controller;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;

public class TilePlacer {

	private static final TilePlacer INSTANCE = new TilePlacer();

	public static TilePlacer instance() {
		return INSTANCE;
	}

	// =========================================

	private void forceUpdates() {
		// TODO: implement update propagation
		System.out.println("Map updated");
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
		this.forceUpdates();
	}

	public TrackTile remove(TileCoordinate c) {
		TrackTile t = Global.map.getTrackPieces().remove(c);
		this.forceUpdates();
		return t;
	}
}
