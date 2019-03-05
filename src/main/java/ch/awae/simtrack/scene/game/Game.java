package ch.awae.simtrack.scene.game;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.Window;
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
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class Game extends Scene<Game> {

	private @Getter PathFinding pathfinder;
	private Model model;
	private ViewPort viewPort;
	private Editor<Game> editor;

	private @Getter AtomicBoolean drawGrid = new AtomicBoolean(true);

	private ToolBar trackbar;
	private DebugTools debugTools;
	private TrainController trainController;

	private @Getter AtomicBoolean paused = new AtomicBoolean(false);

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
		this.editor = new Editor<Game>(this);
		this.trackbar = new ToolBar(editor);
		this.debugTools = new DebugTools(editor);
		this.pathfinder = new PathFinding(model);
		this.trainController = new TrainController(model);
		this.viewPort = new ViewPort(this); //TODO: don't pass game

		editor.addTool(new FreeTool(editor));
		editor.addTool(new BuildTool(editor));
		editor.addTool(new PathFindingTool(editor));
		editor.addTool(new InGameMenu(editor));
		editor.addTool(new InGameSaveMenu(editor));
		editor.addTool(new DebugToolsView(editor, this.debugTools, this.trainController));
		editor.addTool(new SignalTool(editor));

		addRenderer(new BackgroundRenderer());
		addRenderer(new TileRenderer());
		addRenderer(new HexGridRenderer());
		addRenderer(new SignalRenderer());
		addRenderer(new EntityRenderer());
		addRenderer(editor);
		addRenderer(trackbar);
		addRenderer(debugTools.getRenderer());

		addTicker(new Navigator(this, this.viewPort, input));
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
	
}
