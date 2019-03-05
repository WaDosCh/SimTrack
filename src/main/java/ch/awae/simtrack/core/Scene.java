package ch.awae.simtrack.core;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;

public abstract class Scene<T extends Scene<T>> {

	protected @Getter Input input;
	protected @Getter Window window;
	protected final Controller controller;
	protected final Logger logger;

	private @Getter List<BaseRenderer<T>> renderers;
	private @Getter List<BaseTicker<T>> tickers;

	public Scene(Controller controller) {
		renderers = new ArrayList<>();
		tickers = new ArrayList<>();
		this.controller = controller;
		this.input = controller.getInput();
		this.logger = LogManager.getLogger();
	}

	public void addRenderer(BaseRenderer<T> component) {
		renderers.add(component);
	}

	public void addTicker(BaseTicker<T> component) {
		tickers.add(component);
	}
	
	public String getSceneName() {
		return this.getClass().getSimpleName();
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

	public final void requestSnapshot(Consumer<Image> callback) {
		this.controller.requestSnapshot(callback);
	}

	/**
	 * Notifies the scene that a tick is starting and provides the exact time
	 * that passed since the start of the last tick. This value does not
	 * necessarily have to correspond to the time between calls to this method
	 * depending on when the method is being called. It can be expected that the
	 * function is called after rendering and includes uses the times at which
	 * rendering started for the time difference. This should provide the most
	 * possible stability for this value. It is guaranteed that this method will
	 * always be called before the first ticker is invoked.
	 * 
	 * Due to the place in the tick this method is called it should be
	 * implemented with the lowest possible duration. This method is not
	 * monitored by the profiler.
	 * 
	 * The default implementation is empty. Therefore a super call is not
	 * necessary
	 * 
	 * @param millis
	 *            the time since the last tick
	 */
	public void preTick(long millis) {
	}

	/**
	 * Notifies the scene that screen has been resized
	 * 
	 * @param width
	 *            the new width
	 * @param height
	 *            the new height
	 */
	public void screenResized(int width, int height) {
	}

}
