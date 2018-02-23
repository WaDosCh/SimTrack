package ch.awae.simtrack.scene.game;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.*;
import ch.awae.simtrack.scene.game.controller.Navigator;
import ch.awae.simtrack.scene.game.controller.PathFinding;
import ch.awae.simtrack.scene.game.controller.TrainController;
import ch.awae.simtrack.scene.game.controller.tools.*;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.ViewPort;
import ch.awae.simtrack.scene.game.view.renderer.*;
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
	public Game(Controller controller, Model model) {
		super(controller);
		this.model = model;
		this.editor = new Editor<Game>(this);
		this.trackbar = new ToolBar(editor);
		this.debugTools = new DebugTools(editor);
		this.pathfinder = new PathFinding(model);
		this.trainController = new TrainController();

		editor.addTool(new FreeTool(editor));
		editor.addTool(new BuildTool(editor));
		editor.addTool(new PathFindingTool(editor));
		editor.addTool(new InGameMenu(editor));
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

		addTicker(new Navigator(this, input));
		addTicker(trackbar);
		addTicker(editor);
		addTicker(debugTools);
		addTicker("ViewPort", s -> viewPort.tick());
		addTicker("Model", new ProxyTicker(s -> this.model.tick(this)));
		addTicker(pathfinder);
		addTicker(this.trainController);

	}

	/**
	 * moves the view by the given amount
	 * 
	 * @param dx
	 * @param dy
	 */
	public void moveScene(int dx, int dy) {
		this.viewPort.moveScene(dx, dy);
	}

	/**
	 * zooms the view by the given amount at the given point. the given point
	 * remains stationary while zooming.
	 * 
	 * @param dzoom
	 * @param fixX
	 * @param fixY
	 */
	public void zoom(float dzoom, int fixX, int fixY) {
		this.viewPort.zoom((int) (100 * dzoom), fixX, fixY);
	}

	public Model getModel() {
		return this.model;
	}

	public int getHorizontalScreenSize() {
		return window.getCanvasWidth();
	}

	public int getVerticalScreenSize() {
		return window.getCanvasHeight();
	}

	public ViewPort getViewPort() {
		return this.viewPort;
	}

	public void loadModel(Model model) {
		this.model = model;
		this.model.load();
		this.pathfinder.setModel(model);
	}

	@Override
	public void bindWindow(Window window) {
		super.bindWindow(window);
		this.viewPort = new ViewPort(this);
	}

	private class ProxyTicker implements BaseTicker<Game> {

		private BaseTicker<Game> ticker;

		public ProxyTicker(BaseTicker<Game> ticker) {
			this.ticker = ticker;
		}

		@Override
		public void tick(Game scene) {
			if (!scene.paused.get())
				this.ticker.tick(scene);
		}

	}
}
