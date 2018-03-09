package ch.awae.simtrack.core;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.core.Profiler.StringSupplier;
import ch.awae.simtrack.util.Resource;
import lombok.Getter;
import lombok.NonNull;

public class Controller {

	private HighPrecisionClock gameClock;

	private GameWindow window;
	private @Getter final Input input;
	private Stack<Scene<?>> scenes = new Stack<>();
	private Profiler profiler;
	private Binding profilerToggle;
	private boolean showProfiler = false;

	private List<Consumer<Image>> snapshotRequests = new ArrayList<>();

	public Controller(GameWindow window) {
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");
		this.window = window;
		input = new Input();
		profilerToggle = input.getBinding(KeyEvent.VK_F6);
		window.init(input);
	}

	public void start() {
		this.gameClock.start();
	}

	public void stop() {
		this.gameClock.stop();
	}

	private long startOfLastTick = System.currentTimeMillis();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void tick() {

		long newStart = System.currentTimeMillis();
		long deltaT = newStart - startOfLastTick;
		startOfLastTick = newStart;

		if (profiler != null) {
			profiler.startFrame();
			profiler.startSample(true, -1);
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
			snapshot = new BufferedImage(window.getCanvasWidth(), window.getCanvasHeight(), BufferedImage.TYPE_INT_RGB);
			snapshotGraphics = new Graphics(snapshot.createGraphics());
		}

		GameWindow window = this.window;
		window.flipFrame();
		Graphics graphics = window.getGraphics();
		if (scenes.isEmpty())
			return;

		if (profiler == null) {
			createProfiler();
			profiler.startFrame();
		} else {
			profiler.endSample();
		}
		Scene<?> scene = scenes.peek();

		if (window.resized()) {
			scene.screenResized(window.getCanvasWidth(), window.getCanvasHeight());
		}

		int index = 0;
		for (BaseRenderer renderer : scene.getRenderers()) {
			profiler.startSample(true, index);
			GraphicsStack stack = graphics.getStack();
			renderer.render(graphics, scene);
			graphics.setStack(stack);
			if (snapshotGraphics != null) {
				GraphicsStack stack2 = snapshotGraphics.getStack();
				renderer.render(snapshotGraphics, scene);
				snapshotGraphics.setStack(stack2);
			}
			index++;
			profiler.endSample();
		}

		scene.preTick(deltaT);

		index = 0;
		for (BaseTicker ticker : scene.getTickers()) {
			profiler.startSample(false, index);
			ticker.tick(scene);
			index++;
			profiler.endSample();
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

		if (scene != scenes.peek()) {
			scene.onUnload();
			scenes.peek().onLoad();
			profiler = null;
		}

		if (window != this.window) {
			window.discard();
			window.init(input);
			scenes.forEach(sc -> sc.bindWindow(window));
		}
	}

	private void renderProfiler(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		FontMetrics metrics = graphics.getFontMetrics();
		int sh = metrics.getHeight();
		String[] digest = profiler.getDigest().split("\n");
		for (int i = 0; i < digest.length; i++) {
			graphics.drawString(digest[i], 5, (i + 1) * sh);
		}
	}

	@SuppressWarnings("rawtypes")
	private void createProfiler() {
		List<StringSupplier> tickers = new ArrayList<>();
		for (BaseTicker ticker : scenes.peek().getTickers()) {
			tickers.add(ticker::getName);
		}
		List<StringSupplier> renderers = new ArrayList<>();
		for (BaseRenderer renderer : scenes.peek().getRenderers()) {
			renderers.add(renderer::getName);
		}

		int samplingRate = Resource.getProperties("core.properties").getInt("profiler.sampleRate");
		profiler = new Profiler(samplingRate, tickers, renderers);
	}

	public <S extends Scene<S>> void loadScene(@NonNull Scene<S> next) {
		scenes.push(next);
		next.bindWindow(window);
	}

	public void loadRoot() {
		if (scenes.size() == 1)
			return;
		while (scenes.size() > 1)
			scenes.pop();
	}

	public void loadPrevious() {
		if (scenes.size() > 1)
			scenes.pop();
	}

	public <S extends Scene<S>> void replaceWith(@NonNull Scene<S> next) {
		scenes.pop();
		loadScene(next);
	}

	public void setTitle(String title) {
		if (window != null && !scenes.isEmpty())
			this.window.setTitle(scenes.peek().getClass().getSimpleName() + " - " + title);
	}

	public void replaceWindow(GameWindow window) {
		this.window = window;
	}

	public void requestSnapshot(Consumer<Image> callback) {
		snapshotRequests.add(callback);
	}

}
