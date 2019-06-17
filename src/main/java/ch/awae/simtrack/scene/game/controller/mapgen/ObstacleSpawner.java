package ch.awae.simtrack.scene.game.controller.mapgen;

import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.tile.EnvironmentTile;

public class ObstacleSpawner extends Spawner {

	public ObstacleSpawner(Model model, ModelCreationOptions options) {
		super(model, options);
	}

	@Override
	public void spawn(Model model, ModelCreationOptions options) {
		int obstacleCount = options.getArea() / 10;
		for (int i = 0; i < obstacleCount; i++) {
			model.setTileAt(getRandomTile(2), EnvironmentTile.WATER_TILE);
		}
	}
}
