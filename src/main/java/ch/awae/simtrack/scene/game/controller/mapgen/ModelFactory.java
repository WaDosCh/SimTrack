package ch.awae.simtrack.scene.game.controller.mapgen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.utils.functional.Function2;

public class ModelFactory {

	public Model getModel(ModelCreationOptions options) {
		Model model = new Model(options.size, options.startingMoney, options.bulldozeCost);
		List<Spawner> spawners = setupSpawners(model, options);
		// eventually add pre/post steps
		for (Spawner s : spawners) {
			s.spawn(model, options);
		}
		return model;
	}

	private List<Spawner> setupSpawners(Model model, ModelCreationOptions options) {
		List<Function2<Model, ModelCreationOptions, Spawner>> spawners = new ArrayList<>();
		spawners.add(ConnectionSpawner::new);
		spawners.add(FactorySpawner::new);
		spawners.add(ObstacleSpawner::new);
		return spawners.stream().map(c -> c.apply(model, options)).collect(Collectors.toList());
	}

	public Model getDefaultModel() {
		return getModel(new ModelCreationOptions());
	}

}
