package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.CheckboxButton;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Action;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option;
import ch.judos.generic.graphics.ImageUtils;

public class DebugToolsView extends GameTool {

	private BasePanel<Game> renderer;
	private DebugTools debugTools;
	private TrainController trainController;

	public DebugToolsView(Editor<Game> editor, DebugTools debugTools, TrainController trainController) {
		super(editor, true);
		this.debugTools = debugTools;
		this.trainController = trainController;

		this.renderer = new BasePanel<Game>(input, false, this.scene.getWindow());
		this.renderer.margin = 10;
		addButtons(editor.getScene());

		onPress(Action.DEBUG_TOOL, () -> editor.loadTool(FreeTool.class));
	}

	private void addButtons(Game game) {
		this.renderer.add(new Label("Debug Tools"));
		this.renderer
				.add(new CheckboxButton("Show coordinates", input, debugTools.getOptionActive(Option.Coordinates)));
		this.renderer.add(new CheckboxButton("Toggle grid", input, game.getDrawGrid()));
		this.renderer.add(new Button("New map", input, () -> {
			editor.getScene().getSceneController().loadScene(Game.class);
		}));
		this.renderer.add(new CheckboxButton("Pause", input, game.getPaused()));
		this.renderer.add(new Button("Screenshot", input, () -> {
			this.scene.requestSnapshot(image -> {
				try {
					ImageIO.write(ImageUtils.toBufferedImage(image), "jpg", new File("screenshot.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}));

		this.renderer.add(new Label("Trains:"));
		this.renderer.add(new Button("Pathfinding Tool", input, () -> {
			editor.loadTool(PathFindingTool.class);
		}));
		this.renderer
				.add(new CheckboxButton("Show reservations", input, debugTools.getOptionActive(Option.Reservations)));
		this.renderer.add(new Button("Spawn train", input, this.trainController::requestSpawnTrain));
		this.renderer.add(new CheckboxButton("Enable train spawning", input, this.trainController.getActive()));

	}

	@Override
	public void render(Graphics graphics) {
		this.renderer.render(graphics, this.scene.getWindow());
	}

}
