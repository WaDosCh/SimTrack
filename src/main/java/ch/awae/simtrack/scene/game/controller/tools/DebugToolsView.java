package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.BasePanel.PositionH;
import ch.awae.simtrack.core.ui.BasePanel.PositionV;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.CheckboxButton;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option;
import ch.judos.generic.graphics.ImageUtils;

public class DebugToolsView extends GameTool {

	private BasePanel renderer;
	private DebugTools debugTools;
	private TrainController trainController;
	private InputController input;

	public DebugToolsView(Editor editor, DebugTools debugTools, TrainController trainController,
			InputController input) {
		super(editor, true);
		this.debugTools = debugTools;
		this.trainController = trainController;
		this.input = input;

		this.renderer = new BasePanel(PositionH.LEFT, PositionV.TOP, true);
		this.renderer.margin = 10;
		addButtons(editor.getScene());
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DEBUG_TOOL))
			editor.loadTool(FreeTool.class);
		if (!event.isConsumed)
			this.renderer.handleInput(event);
		if (!event.isConsumed)
			super.handleInput(event);
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
		this.renderer.render(graphics);
	}

}
