package ch.awae.simtrack.controller;

import ch.awae.simtrack.scene.BaseRenderer;
import ch.awae.simtrack.scene.BaseTicker;
import ch.awae.simtrack.scene.Buffer;
import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.Scene;
import ch.awae.simtrack.scene.Window;
import ch.awae.simtrack.scene.Graphics.Stack;

public class GameController {

	private HighPrecisionClock gameClock;

	private Window window;
	private Scene<?> scene;

	public GameController(Window window, Scene<?> scene) {
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");
		this.window = window;
		this.scene = scene;
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

		for (BaseRenderer renderer : scene.getRenderers()) {
			Stack stack = graphics.getStack();
			renderer.render(graphics, scene);
			graphics.setStack(stack);
		}

		for (BaseTicker ticker : scene.getTickers()) {
			ticker.tick(scene);
		}

	}

}
