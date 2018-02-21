package ch.awae.simtrack.scene.game.model.tile.track;

import java.util.ArrayList;

import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.scene.game.model.tile.TrackTile;

/**
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FusedTrackFactory {

	public static TrackTile createAnonymousTrack(TilePath[] connections, float cost) {
		return new AnonymousTrack(connections.clone(), cost);
	}

	public static TrackTile createAnonymousTrack(TrackTile tile) {
		return new AnonymousTrack(tile.getRailPaths().clone(), tile.getTravelCost());
	}

	public static TrackTile createFusedTrack(TrackTile tile0, TrackTile tile1) {
		TilePath[] con0 = tile0.getRailPaths().clone();
		TilePath[] con1 = tile1.getRailPaths().clone();

		TilePath[] cons = new TilePath[con0.length + con1.length];

		System.arraycopy(con0, 0, cons, 0, con0.length);
		System.arraycopy(con1, 0, cons, con0.length, con1.length);

		cons = clean(cons);

		return new AnonymousTrack(cons, Math.max(tile0.getTravelCost(), tile1.getTravelCost()));
	}

	private static TilePath[] clean(TilePath[] cons) {
		TrackValidator.sortPathList(cons);
		ArrayList<TilePath> items = new ArrayList<>();
		for (TilePath p : cons)
			items.add(p);
		for (int i = 0; i + 1 < items.size(); i++) {
			if (items.get(i).equals(items.get(i + 1))) {
				items.remove(i + 1);
				i--;
			}
		}
		return items.toArray(new TilePath[0]);
	}
}
