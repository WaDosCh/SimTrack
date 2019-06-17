package ch.awae.simtrack.scene.game.controller.mapgen;

import java.util.Random;

import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;

public abstract class Spawner {
	
	protected ModelCreationOptions options;
	protected Model model;
	protected Random r;

	public Spawner(Model model, ModelCreationOptions options) {
		this.model = model;
		this.options = options;
		this.r = new Random();
	}
	
	// public void preSpawn(Model model, ModelCreationOptions options);

	public abstract void spawn(Model model, ModelCreationOptions options);

	// public void postSpawn(Model model, ModelCreationOptions options);
	
	protected TileCoordinate getRandomTile(int border) {
		int u = r.nextInt(model.getTileGridSize().width - border*2) + border;
		int v = r.nextInt(model.getTileGridSize().height - border*2) + border;
		return new TileCoordinate(u - (v / 2), v);
	}
}
