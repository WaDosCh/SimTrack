package ch.awae.simtrack.scene.game;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
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
import ch.awae.simtrack.scene.game.view.renderer.BackgroundRenderer;
import ch.awae.simtrack.scene.game.view.renderer.EntityRenderer;
import ch.awae.simtrack.scene.game.view.renderer.HexGridRenderer;
import ch.awae.simtrack.scene.game.view.renderer.SignalRenderer;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import lombok.Getter;

public class Game extends Scene {

	private @Getter PathFinding pathfinder;
	private Model model;
	private ViewPortNavigator viewPortNavigator;
	private Editor editor;

	private @Getter AtomicBoolean drawGrid = new AtomicBoolean(true);

	private ToolBar trackbar;
	private DebugTools debugTools;
	private TrainController trainController;

	public Game(Controller controller, Model model, Window window) {
		super(controller, window);
		this.model = model;
		this.model.load();
		InputController input = this.controller.getInput();
		this.editor = new Editor(this); // TODO: don't pass game if possible
		this.trackbar = new ToolBar(this.editor, input);
		this.viewPortNavigator = new ViewPortNavigator(this.model, this.window.getScreenSize(), input);
		this.debugTools = new DebugTools(this.editor, this.viewPortNavigator, this.window, this.model, input);
		this.pathfinder = new PathFinding(this.model);
		this.trainController = new TrainController(this.model);

		editor.addTool(new FreeTool(this.editor, input));
		editor.addTool(new BuildTool(this.editor, this.model, input));
		editor.addTool(new PathFindingTool(this.editor));
		editor.addTool(new InGameMenu(this.editor, input));
		editor.addTool(new InGameSaveMenu(this.editor, this.model, input));
		editor.addTool(new DebugToolsView(this.editor, this.debugTools, this.trainController, input));
		editor.addTool(new SignalTool(this.editor, this.model, input));

		addRenderer(new BackgroundRenderer(this.window));
		addRenderer(new TileRenderer(this.viewPortNavigator, this.model));
		addRenderer(new HexGridRenderer(this.drawGrid, this.viewPortNavigator, this.model));
		addRenderer(new SignalRenderer(this.viewPortNavigator, this.model));
		addRenderer(new EntityRenderer(this.model, this.viewPortNavigator));
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

	public Model getModel() {
		return this.model;
	}

	public ViewPortNavigator getViewPort() {
		return this.viewPortNavigator;
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
