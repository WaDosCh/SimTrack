package ch.awae.simtrack.controller;

import java.util.function.Consumer;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.controller.tools.DebugTools;
import ch.awae.simtrack.controller.tools.ToolBar;
import ch.awae.simtrack.model.Model;
import ch.awae.simtrack.view.GameView;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import lombok.Getter;

/**
 * The game controller implementation
 */
public class GameController implements IController {

	private @Getter Model model;
	private @Getter GameView gameView;
	private @Getter Input input;
	private Navigator navigator;
	private ToolBar trackbar;
	private Editor editor;
	private DebugTools debugTools;
	private @Getter PathFinding pathfinder;
	private Consumer<String> titleUpdater;
	private HighPrecisionClock gameClock;

	/**
	 * instantiates a new controller instance
	 * 
	 * @param model
	 * @param view
	 */
	public GameController(Model model, GameView gameView, IGUIControllerHookup hooker, Input input) {
		this.input = input;
		titleUpdater = hooker.getWindowTitleHookup();
		this.model = model;
		this.gameView = gameView;
		this.gameClock = new HighPrecisionClock(60, this::tick, "Render+Update");
		this.pathfinder = new PathFinding(this.model);
		this.navigator = new Navigator(this.gameView, input);
		this.editor = new Editor(this);
		this.trackbar = new ToolBar(editor);
		this.debugTools = new DebugTools(editor);
		this.gameView.setEditorRenderer(this::render);
	}

	/**
	 * renders the controller-associated elements. There include the editor and
	 * the track-bar
	 * 
	 * @param g
	 *            the graphics instance to render onto
	 * @param v
	 *            the view
	 */
	private void render(Graphics g, IGameView v) {
		this.editor.render(g, v);
		this.debugTools.getRenderer().renderSafe(g, v);
		this.trackbar.getRenderer().renderSafe(g, v);
	}

	@Override
	public void start() {
		this.gameClock.start();
	}

	@Override
	public void stop() {
		this.gameClock.stop();
	}

	/**
	 * performs a game logic update tick.
	 */
	private void tick() {
		// rendering first to make sure this happens at a constant rate when
		// updates take more or less time
		try {
			this.gameView.renderView();
		} catch (NullPointerException nex) {
			nex.printStackTrace();
		}

		// update ticks
		this.navigator.tick();
		this.trackbar.tick();
		this.editor.tick();
		this.debugTools.tick();
		this.gameView.getViewPort().tick();
		this.model.tick();
		this.pathfinder.tick();
	}

	@Override
	public void setWindowTitle(String string) {
		titleUpdater.accept(string);
	}

	@Override
	public void loadModel(Model model) {
		this.model = model;
		this.model.load();
		this.pathfinder.setModel(this.model);
		this.gameView.setModel(this.model);
	}

}
