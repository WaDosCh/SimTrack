package ch.awae.simtrack.core;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ch.awae.utils.functional.Try;
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
		tickers.add(component);
	}

	public void addRenderer(String name, BaseRenderer<T> component) {
		renderers.add(new BaseRenderer<T>() {

			@Override
			public void render(Graphics graphics, T scene) {
				component.render(graphics, scene);
			}

			@Override
			public String getName() {
				return name;
			}
		});
	}

	public void addTicker(String name, BaseTicker<T> component) {
		tickers.add(new BaseTicker<T>() {

			@Override
			public void tick(T scene) {
				component.tick(scene);
			}

			@Override
			public String getName() {
				return name;
			}
		});
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

	public final void requestSnapshot(Consumer<Try<Image>> callback) {
		this.controller.requestSnapshot(callback);
	}
	
}
