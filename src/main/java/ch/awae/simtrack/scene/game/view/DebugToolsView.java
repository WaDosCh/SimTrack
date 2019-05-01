package ch.awae.simtrack.scene.game.view;

import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.CheckboxButton;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.tools.PathFindingTool;
import ch.awae.simtrack.scene.game.model.Model;

public class DebugToolsView extends WindowComponent {

	private TrainController trainController;
	private SceneController sceneController;
	private Model model;
	private Editor editor;

	public DebugToolsView(Editor editor, TrainController trainController, InputController input,
			SceneController sceneController, Model model) {
		super(Design.textFont, input);
		this.editor = editor;
		this.sceneController = sceneController;
		this.trainController = trainController;
		this.model = model;

		this.isVisible = false;
		this.title = "Debug Tools";
		addButtons();
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DEBUG_TOOL))
			this.isVisible = !this.isVisible;
		if (!event.isConsumed)
			super.handleInput(event);
	}

	private void addButtons() {
		addComponent(new Label("Misc:"));
		addComponent(
				new CheckboxButton("Show coordinates", this.input, this.model.getDebugOptions().getShowCoordinates()));
		addComponent(new CheckboxButton("Toggle grid", this.input, this.model.getDrawGrid()));
		addComponent(new Button("New map", this.input, () -> {
			this.sceneController.loadScene(Game.class);
		}));
		addComponent(new CheckboxButton("Pause", this.input, this.model.getIsPaused()));
		addComponent(new Button("Screenshot", this.input, () -> {
			this.sceneController.requestScreenshot();
		}));

		addComponent(new Label("Trains:"));
		addComponent(new Button("Pathfinding Tool", this.input, () -> {
			this.editor.loadTool(PathFindingTool.class);
		}));
		addComponent(new CheckboxButton("Show reservations", this.input,
				this.model.getDebugOptions().getShowTrainReservations()));
		addComponent(new Button("Spawn train", this.input, this.trainController::requestSpawnTrain));
		addComponent(new CheckboxButton("Enable train spawning", this.input, this.trainController.getActive()));

	}

}
