package ch.awae.simtrack.scene.game.model;

public class ModelFactory {

	public static Model getModel(int sizeX, int sizeY, int connectionCount) {
		Model model = new Model(sizeX, sizeY);
		model.load();
		ConnectionSpawner.spawnConnections(model, connectionCount);
		ObstacleSpawner.spawnObstacles(model, sizeX * sizeY / 10);
		return model;
	}

	public static Model getDefaultModel() {
		return getModel(23, 15, 15);
	}

}
