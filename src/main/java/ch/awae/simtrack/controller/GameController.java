package ch.awae.simtrack.controller;

import ch.awae.simtrack.scene.BaseRenderer;
import ch.awae.simtrack.scene.BaseTicker;
import ch.awae.simtrack.scene.Buffer;
import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.Scene;
import ch.awae.simtrack.scene.Window;
import ch.awae.simtrack.scene.Graphics.Stack;
import lombok.NonNull;

public class GameController {

	private HighPrecisionClock gameClock;

	private Window window;
	private java.util.Stack<Scene<?>> scenes = new java.util.Stack<>();

	public GameController(Window window) {
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");
		this.window = window;
	}

	public void start() {
		this.gameClock.start();
	}

	public void stop() {
		this.gameClock.stop();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void tick() {
		Buffer buffer = window.getBuffer();
		buffer.swapBuffer();
		buffer.clearBuffer();
		Graphics graphics = buffer.getGraphics();
		if (scenes.isEmpty())
			return;

		Scene<?> scene = scenes.peek();

		for (BaseRenderer renderer : scene.getRenderers()) {
			Stack stack = graphics.getStack();
			renderer.render(graphics, scene);
			graphics.setStack(stack);
		}

		for (BaseTicker ticker : scene.getTickers()) {
			ticker.tick(scene);
		}

		if (scene != scenes.peek()) {
			scene.onUnload();
			scene.onLoad();
		}

	}

	public <S extends Scene<S>> void loadScene(@NonNull Scene<S> next) {
		scenes.push(next);
		next.bindController(this);
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

}
