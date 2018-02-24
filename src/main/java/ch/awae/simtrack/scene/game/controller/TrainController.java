package ch.awae.simtrack.scene.game.controller;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.PathFindingOptions;
import ch.awae.simtrack.scene.game.model.PathFindingOptions.Type;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.entity.TrainElementConfiguration;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.util.CollectionUtil;
import ch.awae.simtrack.util.DataMapper;
import ch.awae.simtrack.util.Time;
import lombok.Getter;

public class TrainController implements BaseTicker<Game> {

	public static final int firstCheckAfterXSec = 5;
	public static final int checkEveryXSec = 3;

	private Logger logger = LogManager.getLogger(DebugTools.class);
	private int spawnTrains;
	private long checkForSpawnTimeMS;
	private @Getter DataMapper<Boolean> active = new DataMapper.Store<>(false);

	public TrainController() {
		this.checkForSpawnTimeMS = System.currentTimeMillis() + firstCheckAfterXSec * 1000;
	}

	@Override
	public void tick(Game game) {
		while (this.spawnTrains > 0) {
			this.spawnTrains--;
			spawnTrain(game.getModel());
		}
		if (Time.isOver(checkForSpawnTimeMS) && this.active.get()) {
			Model model = game.getModel();
			if (model.getEntitiesByType(Train.class).size() < 3) {
				spawnTrain(model);
			}
			this.checkForSpawnTimeMS = System.currentTimeMillis() + checkEveryXSec * 1000;
		}
	}

	private void spawnTrain(Model model) {
		Set<Entry<TileCoordinate, Tile>> spawners = model.getPossibleSpawnPoints();
		if (spawners.size() == 0) {
			logger.warn("No possible spawn points for a new train.");
			return;
		}
		Entry<TileCoordinate, Tile> spawner = CollectionUtil.randomValue(spawners);
		TileEdgeCoordinate start = model.getPaths(spawner.getKey()).get(0)._2;

		Train t = new Train(model, start, new PathFindingOptions(Type.RandomTarget),
				TrainElementConfiguration.locomotive1);
		model.getEntities().add(t);
		logger.info("Train spawned at " + start);
	}

	public void requestSpawnTrain() {
		this.spawnTrains++;
	}

}
