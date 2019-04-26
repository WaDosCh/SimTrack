package ch.awae.simtrack.scene.game;

import java.awt.Dimension;

import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.controller.tools.BuildTool;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools;
import ch.awae.simtrack.scene.game.controller.tools.DebugToolsView;
import ch.awae.simtrack.scene.game.controller.tools.FreeTool;
import ch.awae.simtrack.scene.game.controller.tools.InGameMenu;
import ch.awae.simtrack.scene.game.controller.tools.InGameSaveMenu;
import ch.awae.simtrack.scene.game.controller.tools.PathFindingTool;
import ch.awae.simtrack.scene.game.controller.tools.SignalTool;
import ch.awae.simtrack.scene.game.controller.tools.ToolBar;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.renderer.MapRenderer;

public class Game extends Scene {

	private PathFinding pathfinder;
	private Window window;
	private Model model;
	private ViewPortNavigator viewPortNavigator;
	private Editor editor;

	private ToolBar trackbar;
	private DebugTools debugTools;
	private TrainController trainController;

	public Game(SceneController sceneController, Model modelToLoad, Window window, InputController input) {
		super(sceneController);
		this.window = window;
		this.model = modelToLoad;
		this.model.load();
		this.editor = new Editor();
		this.viewPortNavigator = new ViewPortNavigator(this.model, this.window.getScreenSize(), input);
		this.trackbar = new ToolBar(this.editor, input, this.viewPortNavigator);
		this.debugTools = new DebugTools(this.editor, this.viewPortNavigator, this.window, this.model, input);
		this.pathfinder = new PathFinding(this.model);
		this.trainController = new TrainController(this.model);

		editor.addTool(new FreeTool(this.editor, input, viewPortNavigator));
		editor.addTool(new BuildTool(this.editor, this.model, input, viewPortNavigator));
		editor.addTool(new PathFindingTool(this.editor, viewPortNavigator, this.pathfinder));
		editor.addTool(new InGameMenu(this.editor, input, this.viewPortNavigator, this.model, this.sceneController));
		editor.addTool(new InGameSaveMenu(this.editor, this.model, input, viewPortNavigator));
		editor.addTool(new DebugToolsView(this.editor, this.debugTools, this.trainController, input, viewPortNavigator,
				this.sceneController, this.model));
		editor.addTool(new SignalTool(this.editor, this.model, input, viewPortNavigator));

		addRenderer(new MapRenderer(this.model, this.viewPortNavigator));
		addRenderer(this.editor);
		addRenderer(this.trackbar);
		addRenderer(this.debugTools.getRenderer());

		addTicker(this.trackbar);
		addTicker(this.editor);
		addTicker(this.debugTools);
		addTicker(this.viewPortNavigator);
		addTicker(this.model);
		addTicker(this.pathfinder);
		addTicker(this.trainController);
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
		this.viewPortNavigator.handleInput(event);
		if (event.isConsumed)
			return;
		this.trackbar.handleInput(event);
		if (event.isConsumed)
			return;
		this.editor.handleInput(event);
		if (event.isConsumed)
			return;
		this.debugTools.handleInput(event);
	}

}
