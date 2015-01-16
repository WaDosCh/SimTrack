package controller;

import java.util.ArrayList;
import java.util.Random;

import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.BorderTrackTile;

/**
 * This basic implementation spawns connections on the edges with a random
 * distribution.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public class BasicBorderConnectionSpawner implements IBorderConnectionSpawner {

	/**
	 * creates a new spawner instance. The same instance can be re-used.
	 * 
	 * @param connectionCount
	 *            the number of connections the spawner should place.
	 */
	public BasicBorderConnectionSpawner(int connectionCount) {
		this.connectionCount = connectionCount;
		this.random = new Random();
	}

	private int connectionCount;
	private Random random;

	@Override
	public ArrayList<BorderTrackTile> spawnConnections(Map map) {

		ArrayList<BorderTrackTile> list = new ArrayList<>();

		for (int i = 0; i < this.connectionCount; i++) {

			final int h = map.getHorizontalSize();
			final int v = map.getVerticalSize();

			// top / bottom edge may spawn anywhere. => 2*h possibilities
			// left / right edge may only spawn on outer tiles. => v - 3
			// possibilities. the corners should not be occupied. => total
			// possible:
			// 2 * h + v - 7

			int slots = 2 * h + v - 7;

			if (slots <= 0)
				return null;

			// STEP 1: choose border
			int slot = this.random.nextInt(slots);

			int U, V, edge;

			// STEP 2: determine position / orient
			if (slot < h - 2) {
				slot += 1;
				// TOP
				U = slot;
				V = 0;
				edge = this.random.nextInt(2) + 4;
			} else if (slot < h - 4) {
				slot -= h - 1;
				// BOTTOM
				U = slot;
				V = v - 1;
				edge = this.random.nextInt(2) + 1;
			} else if (slot < h - 4 + (v - 3) / 2) {
				slot -= h - 3 + (v - 3) / 2;
				// LEFT
				U = -slot;
				V = 2 * slot;
				edge = (this.random.nextInt(3) + 5) % 6;
			} else {
				slot -= h + v - 6;
				// RIGHT
				U = h - 1 - slot;
				V = 2 * slot;
				edge = this.random.nextInt(3) + 2;
			}

			// STEP 3: assemble
			BorderTrackTile tile = new BorderTrackTile(
					new TileCoordinate(U, V), edge, this.random.nextBoolean());

			list.add(tile);
		}
		return list;
	}
}
