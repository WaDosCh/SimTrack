package ch.awae.simtrack.scene.game;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionH;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionV;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.controller.tools.InGameMenu;
import ch.awae.simtrack.scene.game.controller.tools.InGameSaveMenu;
import ch.awae.simtrack.scene.game.controller.tools.ToolBar;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.DebugToolsView;
import ch.awae.simtrack.scene.game.view.InputGuideView;
import ch.awae.simtrack.scene.game.view.renderer.MapRenderer;

public class Game extends Scene {

	private PathFinding pathfinder;
	private Window window;
	private Model model;
	private ViewPortNavigator viewPortNavigator;
	private Editor editor;

	private ToolBar toolbar;
	private TrainController trainController;
	private DesktopComponent ui;
	private DebugToolsView debugWindow;
	private InputController input;

	public Game(SceneController sceneController, Model modelToLoad, Window window, InputController input) {
		super(sceneController);
		this.window = window;
		this.input = input;
		this.model = modelToLoad;
		this.model.load();
		this.viewPortNavigator = new ViewPortNavigator(this.model, this.window.getScreenSize(), input);
		this.pathfinder = new PathFinding(this.model);
		this.trainController = new TrainController(this.model);
		this.editor = new Editor(this.model, this.viewPortNavigator, input, this.pathfinder);
		this.toolbar = new ToolBar(this.editor, input, this.viewPortNavigator);
		this.ui = new DesktopComponent(input);
		setupUI();

		// TODO: remove the following tools, these are UI elements, not tools
		editor.addTool(new InGameMenu(this.editor, input, this.viewPortNavigator, this.model, this.sceneController));
		editor.addTool(new InGameSaveMenu(this.editor, this.model, input, viewPortNavigator));

		addRenderer(new MapRenderer(this.input, this.model, this.viewPortNavigator));
		addRenderer(this.editor);
		addRenderer(this.toolbar);
		addRenderer(this.ui);

		addTicker(this.toolbar);
		addTicker(this.editor);
		addTicker(this.viewPortNavigator);
		addTicker(this.model);
		addTicker(this.pathfinder);
		addTicker(this.trainController);
	}

	private void setupUI() {
		this.ui.layout(0, 0, this.window.getScreenSize().width, this.window.getScreenSize().height);
		this.debugWindow = new DebugToolsView(this.editor, this.trainController, this.input, this.sceneController,
				this.model);
		this.ui.addWindow(this.debugWindow, PositionH.LEFT, PositionV.TOP);

		InputGuideView inputGuide = new InputGuideView(this.input, this.model);
		this.ui.addWindow(inputGuide, PositionH.CENTER, PositionV.CENTER);
	}

	@Override
	public void preTick(long millis) {
		this.model.getClock().tick(millis);
	}

	@Override
	public void screenResized(Dimension screenSize) {
		this.viewPortNavigator.setScreenSize(screenSize);
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
		if (!event.isConsumed)
			this.viewPortNavigator.handleInput(event);
		if (!event.isConsumed)
			this.toolbar.handleInput(event);
		if (!event.isConsumed)
			this.editor.handleInput(event);
	}

	private void toggleInputGuide() {
		AtomicBoolean inputGuide = this.model.getDebugOptions().getShowInputGuide();
		inputGuide.set(!inputGuide.get());
	}

}
