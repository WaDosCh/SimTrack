package ch.awae.simtrack.model;

public class ModelFactory {

	public static IModel getModel(int sizeX, int sizeY, int connectionCount) {
		IModel model = new Model(sizeX, sizeY);
		ConnectionSpawner.spawnConnections(model, connectionCount);
		return model;
	}

}
