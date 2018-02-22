package ch.awae.simtrack.core;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public abstract class Scene<T extends Scene<T>> {

	protected @Getter Input input;
	protected @Getter Window window;
	private final Controller controller;

	private List<BaseRenderer<T>> renderers;
	private List<BaseTicker<T>> tickers;

	public Scene(Controller controller) {
		renderers = new ArrayList<>();
		tickers = new ArrayList<>();
		this.controller = controller;
		this.input = controller.getInput();
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
		this.controller.setTitle(title);
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

	public void bindWindow(Window window) {
		this.window = window;
	}

}
