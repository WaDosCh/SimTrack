package ch.awae.simtrack.controller;

import java.awt.Graphics2D;
import java.util.function.Consumer;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.controller.tools.DebugTools;
import ch.awae.simtrack.controller.tools.TrackBar;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;
import lombok.Getter;

/**
 * The game controller implementation
 */
public class GameController implements IController {

	private @Getter IModel model;
	private @Getter IGameView gameView;
	private @Getter Input input;
	private Navigator navigator;
	private TrackBar trackbar;
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
	public GameController(IModel model, IGameView gameView, IGUIControllerHookup hooker, Input input) {
		this.input = input;
		titleUpdater = hooker.getWindowTitleHookup();
		this.model = model;
		this.gameView = gameView;
		this.gameClock = new HighPrecisionClock(60, this::tick, "Render+Update");
		this.pathfinder = new PathFinding(this.model);
		this.navigator = new Navigator(this.gameView, input);
		this.editor = new Editor(this);
		this.trackbar = new TrackBar(editor);
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
	private void render(Graphics2D g, IGameView v) {
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
		this.gameView.renderView();

		// update ticks
		this.navigator.tick();
		this.editor.tick();
		this.trackbar.tick();
		this.debugTools.tick();
		this.model.tick();
		this.pathfinder.tick();
	}

	@Override
	public void setWindowTitle(String string) {
		titleUpdater.accept(string);
	}

	@Override
	public void loadModel(IModel model) {
		this.model = model;
		this.model.load();
		this.pathfinder.setModel(this.model);
		this.gameView.setModel(this.model);
	}

}
