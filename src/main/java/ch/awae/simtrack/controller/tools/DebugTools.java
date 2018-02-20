package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.model.PathFindingOptions;
import ch.awae.simtrack.model.PathFindingOptions.Type;
import ch.awae.simtrack.model.entity.Train;
import ch.awae.simtrack.model.entity.TrainElementConfiguration;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.model.tile.DestinationTrackTile;
import ch.awae.simtrack.model.tile.Tile;
import ch.awae.simtrack.util.CollectionUtil;
import lombok.Getter;

public class DebugTools extends EventDrivenTool {

	enum Option {
		InputGuide,
		Coordinates;
	}

	private Logger logger = LogManager.getLogger(DebugTools.class);

	private HashSet<Option> showing = new HashSet<Option>();

	private @Getter DebugToolsRenderer renderer = new DebugToolsRenderer(showing, this);

	public DebugTools(Editor editor) {
		super(editor, UnloadAction.IGNORE);

		onPress(KeyEvent.VK_F1, () -> toggle(Option.InputGuide));
		onPress(KeyEvent.VK_F2, () -> toggle(Option.Coordinates));
		onPress(KeyEvent.VK_F3, () -> editor.loadTool(PathFindingTool.class));
		onPress(KeyEvent.VK_F4, this::spawnTrain);
		onPress(KeyEvent.VK_F12, () -> System.exit(0));
	}

	private void spawnTrain() {
		Set<Entry<TileCoordinate, Tile>> spawners = this.model.getTileFiltered(
				tile -> tile instanceof DestinationTrackTile && ((DestinationTrackTile) tile).isTrainSpawner());
		Entry<TileCoordinate, Tile> spawner = CollectionUtil.randomValue(spawners);
		TileEdgeCoordinate start = this.model.getPaths(spawner.getKey()).get(0)._1;

		Train t = new Train(start, new PathFindingOptions(Type.RandomTarget), TrainElementConfiguration.locomotive1);
		this.model.getEntities().add(t);
		logger.info("Train spawned at " + start);
	}

	private void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

}
