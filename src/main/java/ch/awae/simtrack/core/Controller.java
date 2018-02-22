package ch.awae.simtrack.core;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import ch.awae.simtrack.core.Graphics.Stack;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.util.T2;
import lombok.Getter;
import lombok.NonNull;

public class Controller {

	private HighPrecisionClock gameClock;

	private RootWindow window;
	private @Getter final Input input;
	private java.util.Stack<Scene<?>> scenes = new java.util.Stack<>();
	private Profiler profiler;
	private Binding profilerToggle;
	private boolean showProfiler = false;

	public Controller(RootWindow window) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void tick() {
		if (profiler != null) {
			profiler.startFrame();
			profiler.startSample(true, -1);
		}

		if (profilerToggle.isPressed() && profilerToggle.isEdge()) {
			showProfiler = !showProfiler;
			profilerToggle.consume();
		}

		RootWindow window = this.window;
		Buffer buffer = window.getBuffer();

		buffer.swapBuffer();
		buffer.clearBuffer();
		Graphics graphics = buffer.getGraphics();
		if (scenes.isEmpty())
			return;

		if (profiler == null) {
			createProfiler();
			profiler.startFrame();
		} else {
			profiler.endSample();
		}
		Scene<?> scene = scenes.peek();

		int index = 0;
		for (T2 renderer : scene.getRenderers()) {
			profiler.startSample(true, index);
			Stack stack = graphics.getStack();
			((BaseRenderer) renderer._2).render(graphics, scene);
			graphics.setStack(stack);
			index++;
			profiler.endSample();
		}
		index = 0;
		for (T2 ticker : scene.getTickers()) {
			profiler.startSample(false, index);
			((BaseTicker) ticker._2).tick(scene);
			index++;
			profiler.endSample();
		}

		if (showProfiler) {
			graphics.setColor(Color.BLACK);
			FontMetrics metrics = graphics.getFontMetrics();
			int sh = metrics.getHeight();
			String[] digest = profiler.getDigest().split("\n");
			for (int i = 0; i < digest.length; i++) {
				graphics.drawString(digest[i], 5, (i + 1) * sh);
			}
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

	@SuppressWarnings("rawtypes")
	private void createProfiler() {
		List<String> tickers = new ArrayList<>();
		for (T2 ticker : scenes.peek().getTickers()) {
			tickers.add((String) ticker._1);
		}
		List<String> renderers = new ArrayList<>();
		for (T2 renderer : scenes.peek().getRenderers()) {
			renderers.add((String) renderer._1);
		}

		profiler = new Profiler(Resource.getProperties("core.properties").getInt("profiler.sampleRate"), tickers,
				renderers);
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

	public void replaceWindow(RootWindow window) {
		this.window = window;
	}

}
