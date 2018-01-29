package ch.awae.simtrack.model;

public class ModelFactory {

	public static IModel getModel(int sizeX, int sizeY, int connectionCount) {
		IModel model = new Model(sizeX, sizeY);
		model.load();
		ConnectionSpawner.spawnConnections(model, connectionCount);
		ObstacleSpawner.spawnObstacles(model, 15);
		return model;
	}

}
