package ch.awae.simtrack.controller;

import java.awt.Graphics2D;

import javax.swing.Timer;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.tools.TrackBar;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IView;

public class GameController implements IController {

	private IModel model;
	private IView view;
	private Mouse mouse;
	private Keyboard keyboard;
	private Navigator navigator;
	private TrackBar trackbar;
	private Editor editor;
	private int fps, tps;

	private Timer tickTimer, viewTimer;

	public GameController(IModel model, IView view) {
		this.model = model;
		this.view = view;
		this.tickTimer = new Timer(10, e -> this.tick());
		this.tickTimer.setRepeats(true);
		this.viewTimer = new Timer(10, e -> this.viewTick());
		this.viewTimer.setRepeats(true);
		this.navigator = new Navigator(this);
		this.editor = new Editor(this);
		this.trackbar = new TrackBar(this.editor);
		this.view.setEditorRenderer(this::render);
	}

	private void render(Graphics2D g, IView v) {
		this.editor.render(g, v);
		this.trackbar.getRenderer().render(g, v);
	}

	void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}

	void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	@Override
	public IModel getModel() {
		return this.model;
	}

	@Override
	public IView getView() {
		return this.view;
	}

	@Override
	public void setFPS(int fps) {
		this.fps = fps;
		this.viewTimer.setDelay(this.fps);
	}

	@Override
	public void setTPS(int tps) {
		this.tps = tps;
		this.tickTimer.setDelay(this.tps);
	}

	@Override
	public void start() {
		this.tickTimer.start();
	}

	@Override
	public void stop() {
		this.tickTimer.stop();
	}

	@Override
	public void startView() {
		this.viewTimer.start();
	}

	@Override
	public void stopView() {
		this.viewTimer.stop();
	}

	@Override
	public Mouse getMouse() {
		return this.mouse;
	}

	@Override
	public Keyboard getKeyboard() {
		return this.keyboard;
	}

	private void tick() {
		this.navigator.tick();
		this.editor.tick();
		this.trackbar.tick();
		// TODO: tick logic
	}

	private void viewTick() {
		long a = System.nanoTime();
		this.view.renderView();
		System.out.println((System.nanoTime() - a) / 1000);
	}

}
