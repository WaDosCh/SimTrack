package ch.awae.simtrack.core;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.core.profiler.Profiler;
import ch.awae.simtrack.core.profiler.ProfilerI;
import ch.awae.simtrack.util.ReflectionHelper;
import ch.awae.simtrack.util.Resource;
import lombok.Getter;

public class Controller implements SceneController {

	private HighPrecisionClock gameClock;
	protected GameWindow window;
	protected @Getter final Input input;
	protected Scene currentScene = null;
	protected final Logger logger = LogManager.getLogger();

	private ProfilerI profiler;
	private Binding profilerToggle;
	private boolean showProfiler = false;
	private List<Consumer<Image>> snapshotRequests = new ArrayList<>();

	private long startOfLastTick = System.currentTimeMillis();
	private SceneFactory sceneFactory;

	public Controller(GameWindow window) {
		this.window = window;
		this.input = new Input();
		this.sceneFactory = new SceneFactory(this, window);
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");
		profilerToggle = input.getBinding(KeyEvent.VK_F6);

		int samplingRate = Resource.getConfigProperties("core.properties").getInt("profiler.sampleRate");
		profiler = new Profiler(samplingRate);
		window.init(input);
	}

	public void start() {
		this.gameClock.start();
	}

	public void stop() {
		this.gameClock.stop();
	}

	private void tick() {

		long newStart = System.currentTimeMillis();
		long deltaT = newStart - startOfLastTick;
		startOfLastTick = newStart;
		GameWindow window = this.window;

		if (profiler != null) {
			profiler.startFrame();
		}

		if (profilerToggle.isPressed() && profilerToggle.isEdge()) {
			showProfiler = !showProfiler;
			profilerToggle.consume();
		}

		// setup screenshot buffers if required
		List<Consumer<Image>> requests = null;
		BufferedImage snapshot = null;
		Graphics snapshotGraphics = null;
		if (!snapshotRequests.isEmpty()) {
			requests = snapshotRequests;
			snapshotRequests = new ArrayList<>();
			snapshot = new BufferedImage(window.getCanvasSize().width, window.getCanvasSize().height,
					BufferedImage.TYPE_INT_RGB);
			snapshotGraphics = new Graphics(snapshot.createGraphics());
		}

		window.flipFrame();
		Graphics graphics = window.getGraphics();
		if (this.currentScene == null)
			return;

		Scene scene = this.currentScene;

		if (window.resized()) {
			scene.screenResized(window.getCanvasSize().width, window.getCanvasSize().height);
		}

		int index = 0;
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
			index++;
		}

		scene.preTick(deltaT);

		index = 0;
		for (BaseTicker ticker : scene.getTickers()) {
			profiler.startSample(ticker);
			ticker.tick();
			index++;
			profiler.endSample(ticker);
		}

		if (showProfiler) {
			renderProfiler(graphics);
			if (snapshotGraphics != null) {
				renderProfiler(snapshotGraphics);
			}
		}

		if (snapshot != null) {
			// this assertion is here to stop my compiler from complaining about
			// potential null pointers (which is actually impossible, but
			// hey...)
			assert (snapshotGraphics != null && requests != null);
			snapshotGraphics.dispose();
			for (Consumer<Image> callback : requests)
				callback.accept(snapshot);
			// requester list does not need to be cleared, as it has replaced
		}

		profiler.endFrame();

	}

	private void renderProfiler(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		FontMetrics metrics = graphics.getFontMetrics();
		int sh = metrics.getHeight();
		String[] digest = profiler.getProfilerOutput().split("\n");
		for (int i = 0; i < digest.length; i++) {
			graphics.drawString(digest[i], 5, (i + 1) * sh);
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
				ReflectionHelper<?> reflector = new ReflectionHelper<>(this.currentScene);
				reflector.findAndInvokeCompatibleMethod(OnUnload.class, null);
			}

			logger.debug("loading scene " + scene);
			ReflectionHelper<?> reflector = new ReflectionHelper<>(scene);
			reflector.findAndInvokeCompatibleMethod(OnLoad.class, null);
			this.currentScene = scene;
		}
		return this.currentScene;
	}

}
