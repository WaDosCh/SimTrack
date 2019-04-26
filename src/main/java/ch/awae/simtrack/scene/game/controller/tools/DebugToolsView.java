package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.CheckboxButton;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionH;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionV;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option;
import ch.awae.simtrack.scene.game.model.Model;
import ch.judos.generic.graphics.ImageUtils;

public class DebugToolsView extends GameTool {

	private BasePanel uiPanel;
	private DebugTools debugTools;
	private TrainController trainController;
	private InputController input;
	private SceneController sceneController;
	private Model model;

	public DebugToolsView(Editor editor, DebugTools debugTools, TrainController trainController,
			InputController input, ViewPortNavigator viewPort, SceneController sceneController, Model model) {
		super(editor, viewPort, true);
		this.sceneController = sceneController;
		this.debugTools = debugTools;
		this.trainController = trainController;
		this.input = input;
		this.model = model;

		this.uiPanel = new BasePanel(PositionH.LEFT, PositionV.TOP, true);
		this.uiPanel.margin = 10;
		addButtons();
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DEBUG_TOOL))
			editor.loadTool(FreeTool.class);
		if (!event.isConsumed)
			this.uiPanel.handleInput(event);
		if (!event.isConsumed)
			super.handleInput(event);
	}

	private void addButtons() {
		this.uiPanel.add(new Label("Debug Tools"));
		this.uiPanel
				.add(new CheckboxButton("Show coordinates", input, debugTools.getOptionActive(Option.Coordinates)));
		this.uiPanel.add(new CheckboxButton("Toggle grid", input, this.model.getDrawGrid()));
		this.uiPanel.add(new Button("New map", input, () -> {
			this.sceneController.loadScene(Game.class);
		}));
		this.uiPanel.add(new CheckboxButton("Pause", input, this.model.getIsPaused()));
		this.uiPanel.add(new Button("Screenshot", input, () -> {
			this.sceneController.requestSnapshot(image -> {
				try {
					ImageIO.write(ImageUtils.toBufferedImage(image), "jpg", new File("screenshot.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}));

		this.uiPanel.add(new Label("Trains:"));
		this.uiPanel.add(new Button("Pathfinding Tool", input, () -> {
			editor.loadTool(PathFindingTool.class);
		}));
		this.uiPanel
				.add(new CheckboxButton("Show reservations", input, debugTools.getOptionActive(Option.Reservations)));
		this.uiPanel.add(new Button("Spawn train", input, this.trainController::requestSpawnTrain));
		this.uiPanel.add(new CheckboxButton("Enable train spawning", input, this.trainController.getActive()));

	}

	@Override
	public void render(Graphics graphics) {
		this.uiPanel.render(graphics);
	}

}
