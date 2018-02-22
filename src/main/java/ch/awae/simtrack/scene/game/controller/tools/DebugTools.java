package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.TrainController;

public class DebugTools extends GameTool {

	enum Option {
		InputGuide,
		Coordinates,
		Reservations;
	}

	private HashSet<Option> showing = new HashSet<Option>();

	private DebugToolsRenderer renderer = new DebugToolsRenderer(showing, this);

	public TrainController trainController;

	private boolean pause;

	public DebugTools(Editor<Game> editor) {
		super(editor, GameTool.UnloadAction.IGNORE);

		onPress(KeyEvent.VK_F1, () -> toggle(Option.InputGuide));
		onPress(KeyEvent.VK_F2, () -> toggle(Option.Coordinates));
		onPress(KeyEvent.VK_F8, () -> toggle(Option.Reservations));
		onPress(KeyEvent.VK_F3, () -> editor.loadTool(PathFindingTool.class));
		onPress(KeyEvent.VK_F4, this::spawnTrain);
		onPress(KeyEvent.VK_F5, () -> editor.getScene().toggleHex());
		onPress(KeyEvent.VK_F7, this::pause);
		onPress(KeyEvent.VK_F12, () -> System.exit(0));
	}

	private void pause() {
		this.pause = !this.pause;
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
	public void tick(Game scene) {
		super.tick(scene);
		scene.setPaused(this.pause);
	}

	@Override
	public void render(Graphics graphics, Game scene) {
		this.renderer.render(graphics, scene);
	}

}
