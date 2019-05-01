package ch.awae.simtrack.scene.game.controller;

import java.util.Random;

import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.EnvironmentTile;

public class ObstacleSpawner {

	public static void spawnObstacles(Model model, int amountOfObstacles) {
		Random r = new Random();

		for (int i = 0; i < amountOfObstacles; i++) {

			int u = r.nextInt(model.getHorizontalSize() - 4) + 2;
			int v = r.nextInt(model.getVerticalSize() - 4) + 2;

			TileCoordinate tileCoordinate = new TileCoordinate(u - (v / 2), v);

			model.setTileAt(tileCoordinate, EnvironmentTile.WATER_TILE);
		}
	}
}
