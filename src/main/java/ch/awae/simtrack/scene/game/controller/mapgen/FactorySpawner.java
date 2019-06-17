package ch.awae.simtrack.scene.game.controller.mapgen;

import ch.awae.simtrack.scene.game.model.Goods;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.Factory;
import ch.judos.generic.data.DynamicList;

public class FactorySpawner extends Spawner {

	public FactorySpawner(Model model, ModelCreationOptions options) {
		super(model, options);
	}

	@Override
	public void spawn(Model model, ModelCreationOptions options) {
		int factoryCount = options.getArea() * options.startingFactories / 100;
		spawnFactories(model, factoryCount);
	}

	private void spawnFactories(Model model, int factoryCount) {
		for (int i = 0; i < factoryCount; i++)
			spawnFactory(model);
	}

	private void spawnFactory(Model model) {
		TileCoordinate tile = getRandomTile(3);
		DynamicList<Goods> goods = new DynamicList<Goods>(Goods.values());
		Goods produce = goods.getRandomElement();
		model.setTileAt(tile, new Factory(produce));
	}

}
