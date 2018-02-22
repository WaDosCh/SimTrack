package ch.awae.simtrack.core;

import ch.awae.simtrack.core.Graphics.Stack;
import lombok.Getter;
import lombok.NonNull;

public class Controller {

	private HighPrecisionClock gameClock;

	private RootWindow window;
	private @Getter final Input input;
	private java.util.Stack<Scene<?>> scenes = new java.util.Stack<>();

	public Controller(RootWindow window) {
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");
		this.window = window;
		input = new Input();
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
		RootWindow window = this.window;
		window.flipFrame();
		Graphics graphics = window.getGraphics();
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

		if (window != this.window) {
			window.discard();
			window.init(input);
			scenes.forEach(sc -> sc.bindWindow(window));
		}
		
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
