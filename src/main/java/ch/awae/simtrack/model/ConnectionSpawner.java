package ch.awae.simtrack.model;

import java.util.ArrayList;
import java.util.Random;

import ch.awae.simtrack.model.Signal.Type;
import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;

/**
 * This basic implementation spawns connections on the edges with a random
 * distribution.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
class ConnectionSpawner {

	private static Random RAND = new Random();

	public static void spawnConnections(IModel model, int amount) {

		ArrayList<TileCoordinate> list = new ArrayList<>();

		for (int i = 0; i < amount; i++) {

			final int ho = model.getHorizontalSize();
			final int ve = model.getVerticalSize();

			boolean isHo = RAND.nextInt(ho + ve / 2) < ho;
			boolean isTo = RAND.nextBoolean();

			int r1 = RAND.nextInt(isHo ? ho : ((ve + 1) / 2));

			int edge, u, v;
			// STEP 2: determine position / orient
			if (isHo) {
				v = isTo ? 0 : ve - 1;
				u = r1 - v / 2;
				if (r1 == 0)
					// left corners
					edge = isTo ? 1 : 5;
				else if (r1 == ho - 1)
					// right corners
					edge = isTo ? 2 : 4;
				else if (isTo)
					// top edge
					edge = RAND.nextBoolean() ? 1 : 2;
				else
					// bottom edge
					edge = RAND.nextBoolean() ? 4 : 5;
			} else {
				r1 += 2;
				r1 *= 2;
				v = r1;
				if (r1 + 1 >= ve)
					continue;
				u = (isTo ? 0 : ho - 1) - (v / 2);
				edge = RAND.nextInt(3) + (isTo ? 5 : 2);
				if (edge > 5)
					edge -= 6;
			}

			TileCoordinate pos = new TileCoordinate(u, v);
			if (!list.contains(pos)) {
				boolean output = RAND.nextBoolean();
				BorderTrackTile tile = BorderTrackTile.getInstance(Edge.byIndex(edge), output);
				model.setTileAt(pos, tile);
				if (!output) {
					TileEdgeCoordinate tec = new TileEdgeCoordinate(pos, Edge.byIndex(edge));
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
