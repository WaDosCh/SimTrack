package ch.awae.simtrack.scene.game.model;

import java.awt.Dimension;

public class ModelFactory {

	public static Model getModel(ModelCreationOptions options) {
		Model model = new Model(options.size, options.startingMoney);
		ConnectionSpawner.spawnConnections(model, options.connectionCount);
		int obstacleCount = options.size.width * options.size.height / 10;
		ObstacleSpawner.spawnObstacles(model, obstacleCount);
		return model;
	}

	public static Model getDefaultModel() {
		ModelCreationOptions defaults = new ModelCreationOptions();
		defaults.size = new Dimension(14, 8);
		defaults.connectionCount = 3;
		defaults.startingMoney = 1000;
		return getModel(defaults);
	}

}
