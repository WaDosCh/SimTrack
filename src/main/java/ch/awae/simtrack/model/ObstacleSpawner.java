package ch.awae.simtrack.model;

import java.util.Random;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.tile.BasicFixedTile;
import ch.awae.simtrack.model.tile.TileType;

public class ObstacleSpawner {

	public static void spawnObstacles(Model model, int amountOfObstacles) {
		Random r = new Random();

		for (int i = 0; i < amountOfObstacles; i++) {

			int u = r.nextInt(model.getHorizontalSize() - 4) + 2;
			int v = r.nextInt(model.getVerticalSize() - 4) + 2;

			TileCoordinate tileCoordinate = new TileCoordinate(u - (v / 2), v);

			model.setTileAt(tileCoordinate, BasicFixedTile.getInstance(TileType.OBSTACLE));
		}
	}
}
