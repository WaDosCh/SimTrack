package ch.awae.simtrack.scene.game;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Navigator;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.TrainController;
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
import ch.awae.simtrack.scene.game.view.ViewPort;
import ch.awae.simtrack.scene.game.view.renderer.BackgroundRenderer;
import ch.awae.simtrack.scene.game.view.renderer.EntityRenderer;
import ch.awae.simtrack.scene.game.view.renderer.HexGridRenderer;
import ch.awae.simtrack.scene.game.view.renderer.SignalRenderer;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import lombok.Getter;

/**
 * the game view implementation
 */
public class Game extends Scene {

	private @Getter PathFinding pathfinder;
	private Model model;
	private ViewPort viewPort;
	private Editor editor;

	private @Getter AtomicBoolean drawGrid = new AtomicBoolean(true);

	private ToolBar trackbar;
	private DebugTools debugTools;
	private TrainController trainController;

	private @Getter AtomicBoolean paused = new AtomicBoolean(false);
	private Navigator navigator;

	/**
	 * instantiates a new game view
	 * 
	 * @param model
	 * @param screenX
	 * @param screenY
	 */
	public Game(Controller controller, Model model, Window window) {
		super(controller, window);
		this.model = model;
		this.model.load(this.getPaused());
		InputController input = this.controller.getInput();
		this.editor = new Editor(this); // TODO: don't pass game if possible
		this.trackbar = new ToolBar(this.editor, input);
		this.viewPort = new ViewPort(this); // TODO: don't pass game if possible
		this.debugTools = new DebugTools(this.editor, this.viewPort, this.window, this.model, input);
		this.pathfinder = new PathFinding(this.model);
		this.trainController = new TrainController(this.model);
		this.navigator = new Navigator(this, this.viewPort, this.controller.getInput());

		editor.addTool(new FreeTool(this.editor, input));
		editor.addTool(new BuildTool(this.editor, this.model, input));
		editor.addTool(new PathFindingTool(this.editor));
		editor.addTool(new InGameMenu(this.editor, input));
		editor.addTool(new InGameSaveMenu(this.editor, this.model, input));
		editor.addTool(new DebugToolsView(this.editor, this.debugTools, this.trainController, input));
		editor.addTool(new SignalTool(this.editor, this.model, input));

		addRenderer(new BackgroundRenderer(this.window));
		addRenderer(new TileRenderer(this.viewPort, this.model));
		addRenderer(new HexGridRenderer(this.drawGrid, this.viewPort, this.model));
		addRenderer(new SignalRenderer(this.viewPort, this.model));
		addRenderer(new EntityRenderer(this.model, this.viewPort));
		addRenderer(this.editor);
		addRenderer(this.trackbar);
		addRenderer(this.debugTools.getRenderer());

		addTicker(this.navigator);
		addTicker(this.trackbar);
		addTicker(this.editor);
		addTicker(this.debugTools);
		addTicker(this.viewPort);
		addTicker(this.model);
		addTicker(this.pathfinder);
		addTicker(this.trainController);
	}

	public Model getModel() {
		return this.model;
	}

	public ViewPort getViewPort() {
		return this.viewPort;
	}

	@Override
	public void preTick(long millis) {
		model.getClock().tick(millis);
	}

	@Override
	public void screenResized(int width, int height) {
		viewPort.update();
	}

	@Override
	public void handleInput(InputEvent event) {
		this.navigator.handleInput(event);
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
