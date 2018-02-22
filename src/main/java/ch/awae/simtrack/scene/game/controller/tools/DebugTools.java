package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.TrainController;

public class DebugTools extends GameTool {

	enum Option {
		InputGuide,
		Coordinates;
	}

	private Logger logger = LogManager.getLogger(DebugTools.class);

	private HashSet<Option> showing = new HashSet<Option>();

	private DebugToolsRenderer renderer = new DebugToolsRenderer(showing, this);

	public TrainController trainController;

	public DebugTools(Editor<Game> editor) {
		super(editor, GameTool.UnloadAction.IGNORE);

		onPress(KeyEvent.VK_F1, () -> toggle(Option.InputGuide));
		onPress(KeyEvent.VK_F2, () -> toggle(Option.Coordinates));
		onPress(KeyEvent.VK_F3, () -> editor.loadTool(PathFindingTool.class));
		onPress(KeyEvent.VK_F4, this::spawnTrain);
		onPress(KeyEvent.VK_F5, () -> editor.getScene().toggleHex());
		onPress(KeyEvent.VK_F12, () -> System.exit(0));
	}

	private void spawnTrain() {
		if (this.trainController != null)
			this.trainController.requestSpawnTrain();
	}

	private void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

	@Override
	public void render(Graphics graphics, Game scene) {
		this.renderer.render(graphics, scene);
	}

}
