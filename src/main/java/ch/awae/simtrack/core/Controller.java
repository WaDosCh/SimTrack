package ch.awae.simtrack.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.profiler.Profiler;
import ch.awae.simtrack.core.profiler.ProfilerI;
import lombok.Getter;

public class Controller implements SceneController {

	private HighPrecisionClock gameClock;
	protected GameWindow window;
	protected @Getter final InputController input;
	protected Scene currentScene = null;
	protected final Logger logger = LogManager.getLogger();

	private ProfilerI profiler;
	private List<Consumer<Image>> snapshotRequests = new ArrayList<>();

	private long startOfLastTick = System.currentTimeMillis();
	private SceneFactory sceneFactory;

	public Controller(GameWindow window) {
		this.window = window;
		this.input = window.getInput();
		this.sceneFactory = new SceneFactory(this, window);
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");

		profiler = new Profiler();
	}

	public void start() {
		this.gameClock.start();
	}

	public void stop() {
		this.gameClock.stop();
	}

	/**
	 * Main loop of the game, rendering, updating everything
	 */
	private void tick() {

		long newStart = System.currentTimeMillis();
		long deltaT = newStart - startOfLastTick;
		startOfLastTick = newStart;
		GameWindow window = this.window;

		profiler.startFrame();

		profiler.startSample(this.input);

		handleAllInputEvents();

		profiler.endSample(this.input);

		// setup screenshot buffers if required
		BufferedImage snapshot = null;
		Graphics snapshotGraphics = null;
		if (!snapshotRequests.isEmpty()) {
			snapshot = new BufferedImage(window.getScreenSize().width, window.getScreenSize().height,
					BufferedImage.TYPE_INT_RGB);
			snapshotGraphics = new Graphics(snapshot.createGraphics());
		}

		window.flipFrame();
		Graphics graphics = window.getGraphics();
		// clip makes everyone aware of the available screen size
		graphics.clipRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		if (this.currentScene == null)
			return;

		Scene scene = this.currentScene;

		if (window.resized()) {
			scene.screenResized(window.getScreenSize().width, window.getScreenSize().height);
		}

		for (BaseRenderer renderer : scene.getRenderers()) {
			profiler.startSample(renderer);
			GraphicsStack stack = graphics.getStack();
			renderer.render(graphics);
			graphics.setStack(stack);
			if (snapshotGraphics != null) {
				GraphicsStack stack2 = snapshotGraphics.getStack();
				renderer.render(snapshotGraphics);
				snapshotGraphics.setStack(stack2);
			}
			profiler.endSample(renderer);
		}

		scene.preTick(deltaT);

		for (BaseTicker ticker : scene.getTickers()) {
			profiler.startSample(ticker);
			ticker.tick();
			profiler.endSample(ticker);
		}

		this.profiler.render(graphics);
		if (snapshotGraphics != null) {
			this.profiler.render(snapshotGraphics);
		}

		if (snapshot != null) {
			// this assertion is here to stop my compiler from complaining about
			// potential null pointers (which is actually impossible, but
			// hey...)
			assert (snapshotGraphics != null);
			snapshotGraphics.dispose();
			for (Consumer<Image> callback : this.snapshotRequests)
				callback.accept(snapshot);
			this.snapshotRequests.clear();
		}

		profiler.endFrame();

	}

	private void handleAllInputEvents() {
		for (InputEvent event : this.input.popAllEvents()) {
			if (event.isPressActionAndConsume(InputAction.QUIT_GAME))
				System.exit(0);
			this.input.handleInput(event);
			if (event.isConsumed)
				continue;
			this.profiler.handleInput(event);
			if (event.isConsumed)
				continue;
			this.currentScene.handleInput(event);
			if (event.isConsumed)
				continue;
		}
	}

	public void requestSnapshot(Consumer<Image> callback) {
		snapshotRequests.add(callback);
	}

	@Override
	public Scene loadScene(Class<? extends Scene> sceneClass, Object... args) {
		Scene scene = this.sceneFactory.createScene(sceneClass, args);
		if (scene != null) {
			if (this.currentScene != null) {
				logger.debug("unloading scene " + this.currentScene);
				this.currentScene.unloadScene();
			}

			logger.debug("loading scene " + scene);
			scene.loadScene();
			this.currentScene = scene;
		}
		return this.currentScene;
	}

}
