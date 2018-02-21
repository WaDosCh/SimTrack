package ch.awae.simtrack.scene;

import java.util.ArrayList;
import java.util.List;

import ch.awae.simtrack.controller.GameController;
import ch.awae.simtrack.controller.input.Input;
import lombok.Getter;

public class Scene<T extends Scene<T>> {

	protected final @Getter int width, height;
	protected final @Getter Input input;
	protected final @Getter Window window;
	private GameController controller;

	private List<BaseRenderer<T>> renderers;
	private List<BaseTicker<T>> tickers;

	public Scene(Window window) {
		this.window = window;
		width = window.getCanvasWidth();
		height = window.getCanvasHeight();
		input = window.getInput();

		renderers = new ArrayList<>();
		tickers = new ArrayList<>();
	}

	public void addRenderer(BaseRenderer<T> component) {
		renderers.add(component);
	}

	public void addTicker(BaseTicker<T> component) {
		tickers.add((BaseTicker<T>) component);
	}

	public List<BaseRenderer<T>> getRenderers() {
		return renderers;
	}

	public List<BaseTicker<T>> getTickers() {
		return tickers;
	}

	public void setWindowTitle(String title) {
		this.window.setTitle(title);
	}

	public final void bindController(GameController controller) {
		this.controller = controller;
	}

	public final <S extends Scene<S>> void transitionTo(Scene<S> next) {
		controller.loadScene(next);
	}

	public final void transitionBack() {
		controller.loadPrevious();
	}

	public final void transitionToHome() {
		controller.loadRoot();
	}

	public void onLoad() {
	}

	public void onUnload() {
	}

}
