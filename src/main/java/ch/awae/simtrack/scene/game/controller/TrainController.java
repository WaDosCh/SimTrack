package ch.awae.simtrack.scene.game.controller;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.PathFindingOptions;
import ch.awae.simtrack.scene.game.model.PathFindingOptions.Type;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.entity.TrainElementConfiguration;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.util.CollectionUtil;
import ch.awae.simtrack.util.Time;
import lombok.Getter;

public class TrainController implements BaseTicker {

	public static final int firstCheckAfterXSec = 5;
	public static final int checkEveryXSec = 3;

	private Logger logger = LogManager.getLogger(getClass());
	private int spawnTrains;
	private long checkForSpawnTimeMS;
	private @Getter AtomicBoolean active = new AtomicBoolean(false);
	private Model model;

	public TrainController(Model model) {
		this.model = model;
		this.checkForSpawnTimeMS = System.currentTimeMillis() + firstCheckAfterXSec * 1000;
	}

	@Override
	public void tick() {
		while (this.spawnTrains > 0) {
			this.spawnTrains--;
			spawnTrain(this.model);
		}
		if (Time.isOver(checkForSpawnTimeMS) && this.active.get()) {
			if (this.model.getEntitiesByType(Train.class).size() < 3) {
				spawnTrain(this.model);
			}
			this.checkForSpawnTimeMS = System.currentTimeMillis() + checkEveryXSec * 1000;
		}
	}

	private void spawnTrain(Model model) {
		Set<Entry<TileCoordinate, BorderTrackTile>> spawners = model.getTileFiltered(BorderTrackTile.class,
				tile -> tile.isTrainSpawner());
		spawners = spawners.stream().filter(entry -> {
			return !this.model.isTileReserved(entry.getKey());
		}).collect(Collectors.toSet());

		if (spawners.size() == 0) {
			logger.warn("No possible spawn points for a new train.");
			return;
		}
		Entry<TileCoordinate, BorderTrackTile> entry = CollectionUtil.randomValue(spawners);
		BorderTrackTile spawner = entry.getValue();
		TileEdgeCoordinate start = new TileEdgeCoordinate(entry.getKey(), spawner.getStartingEdge());

		Train t = new Train(model, start, new PathFindingOptions(Type.RandomTarget),
				TrainElementConfiguration.locomotive1);
		model.getEntities().add(t);
		logger.debug("Train {} spawned at {}", t, start);
	}

	public void requestSpawnTrain() {
		this.spawnTrains++;
	}

}
