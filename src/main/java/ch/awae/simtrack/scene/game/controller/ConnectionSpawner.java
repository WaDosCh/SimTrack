package ch.awae.simtrack.scene.game.controller;

import java.util.ArrayList;
import java.util.Random;

import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.ConstructionTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackProvider;
import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;

/**
 * This basic implementation spawns connections on the edges with a random distribution.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class ConnectionSpawner {

	private static Random RAND = new Random();

	public static void spawnConnections(Model model, int amount) {

		ArrayList<TileCoordinate> list = new ArrayList<>();

		for (int i = 0; i < amount; i++) {

			final int ho = model.getTileGridSize().width;
			final int ve = model.getTileGridSize().height;

			boolean isHo = RAND.nextInt(ho + ve / 2) < ho;
			boolean isTo = RAND.nextBoolean();

			int r1 = RAND.nextInt(isHo ? ho : ((ve + 1) / 2));

			int edgeNr, u, v;
			// STEP 2: determine position / orient
			if (isHo) {
				v = isTo ? 0 : ve - 1;
				u = r1 - v / 2;
				if (r1 == 0)
					// left corners
					edgeNr = isTo ? 1 : 5;
				else if (r1 == ho - 1)
					// right corners
					edgeNr = isTo ? 2 : 4;
				else if (isTo)
					// top edge
					edgeNr = RAND.nextBoolean() ? 1 : 2;
				else
					// bottom edge
					edgeNr = RAND.nextBoolean() ? 4 : 5;
			} else {
				r1 += 2;
				r1 *= 2;
				v = r1;
				if (r1 + 1 >= ve)
					continue;
				u = (isTo ? 0 : ho - 1) - (v / 2);
				edgeNr = RAND.nextInt(3) + (isTo ? 5 : 2);
				if (edgeNr > 5)
					edgeNr -= 6;
			}

			TileCoordinate pos = new TileCoordinate(u, v);
			ConstructionTrackTile straightTrack = TrackProvider.getTiles().get(0);

			if (!list.contains(pos)) {
				// spawn border track
				boolean output = RAND.nextBoolean();
				Edge edge = Edge.byOrdinal(edgeNr);
				BorderTrackTile tile = new BorderTrackTile(edge, output);
				model.setTileAt(pos, tile);

				// spawn extended tracks behind border track tile
				edge = edge.getOpposite();
				TileCoordinate behind = pos;
				for (int j = 0; j < 4; j++) {
					ConstructionTrackTile track = straightTrack.rotated(edgeNr);
					behind = behind.getNeighbour(edge);
					if (model.getTileAt(behind) instanceof TrackTile) {
						TrackTile existingTrack = (TrackTile) model.getTileAt(behind);
						track = track.fuseWith(existingTrack);
						model.removeTileAt(behind);
					}
					model.setTileAt(behind, track);
				}

				if (!output) {
					TileEdgeCoordinate tec = new TileEdgeCoordinate(pos, Edge.byOrdinal(edgeNr));
					Signal signal = new Signal(tec, Type.ONE_WAY);
					model.setSignalAt(tec, signal);
				}
				list.add(pos);
			} else {
				i--;
				continue;
			}

		}

	}
}
