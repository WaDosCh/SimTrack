package ch.awae.simtrack.core;

import java.util.ArrayList;
import java.util.List;

import ch.awae.simtrack.util.T2;
import lombok.Getter;

public abstract class Scene<T extends Scene<T>> {

	protected @Getter Input input;
	protected @Getter Window window;
	private final Controller controller;

	private List<T2<String, BaseRenderer<T>>> renderers;
	private List<T2<String, BaseTicker<T>>> tickers;

	public Scene(Controller controller) {
		renderers = new ArrayList<>();
		tickers = new ArrayList<>();
		this.controller = controller;
		this.input = controller.getInput();
	}

	public void addRenderer(BaseRenderer<T> component) {
		addRenderer(component.getClass().getSimpleName(), component);
	}

	public void addTicker(BaseTicker<T> component) {
		addTicker(component.getClass().getSimpleName(), component);
	}

	public void addRenderer(String name, BaseRenderer<T> component) {
		renderers.add(new T2<>(name, component));
	}
	
	public void addTicker(String name, BaseTicker<T> component) {
		tickers.add(new T2<>(name, component));
	}

	public List<T2<String, BaseRenderer<T>>> getRenderers() {
		return renderers;
	}

	public List<T2<String, BaseTicker<T>>> getTickers() {
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
