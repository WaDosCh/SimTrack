package ch.awae.simtrack.model;

import java.util.Random;

import ch.awae.simtrack.model.position.TileCoordinate;

public class ObstacleSpawner {

	public static void spawnObstacles(IModel model, int amountOfObstacles) {
		Random r = new Random();

		for (int i = 0; i < amountOfObstacles; i++) {

			int u = r.nextInt(model.getHorizontalSize());
			int v = r.nextInt(model.getVerticalSize());

			TileCoordinate tileCoordinate = new TileCoordinate(u, v);
			ObstacleTile obstacle = new ObstacleTile();

			model.setTileAt(tileCoordinate, obstacle);
		}
	}
}
