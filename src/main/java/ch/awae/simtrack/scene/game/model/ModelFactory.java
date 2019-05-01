package ch.awae.simtrack.scene.game.model;

public class ModelFactory {

	public static Model getModel(ModelCreationOptions options) {
		Model model = new Model(options.size, options.startingMoney, options.bulldozeCost);
		ConnectionSpawner.spawnConnections(model, options.connectionCount);
		int obstacleCount = options.size.width * options.size.height / 10;
		ObstacleSpawner.spawnObstacles(model, obstacleCount);
		return model;
	}

	public static Model getDefaultModel() {
		return getModel(new ModelCreationOptions());
	}

}
