/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.controller;

import java.awt.Graphics2D;

import javax.swing.Timer;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.tools.DebugTools;
import ch.awae.simtrack.controller.tools.TrackBar;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;
import lombok.Getter;

/**
 * The game controller implementation
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
class GameController implements IController {

	private @Getter IModel model;
	private @Getter IGameView gameView;
	private Mouse mouse;
	private Keyboard keyboard;
	private Navigator navigator;
	private TrackBar trackbar;
	private Editor editor;
	private int fps, tps;

	private Timer tickTimer, viewTimer;
	private DebugTools debugTools;

	/**
	 * instantiates a new controller instance
	 * 
	 * @param model
	 * @param view
	 */
	public GameController(IModel model, IGameView view) {
		this.model = model;
		this.gameView = view;
		this.tickTimer = new Timer(10, e -> this.tick());
		this.tickTimer.setRepeats(true);
		this.viewTimer = new Timer(10, e -> this.viewTick());
		this.viewTimer.setRepeats(true);
		this.navigator = new Navigator(this);
		this.editor = new Editor(this);
		this.trackbar = new TrackBar(this.editor);
		this.debugTools = new DebugTools(this.editor);
		this.gameView.setEditorRenderer(this::render);
	}

	/**
	 * renders the controller-associated elements. There include the editor and
	 * the track-bar
	 * 
	 * @param g
	 *            the graphics instance to render onto
	 * @param v
	 *            the view
	 */
	private void render(Graphics2D g, IGameView v) {
		this.editor.render(g, v);
		this.trackbar.getRenderer().render(g, v);
		this.debugTools.getRenderer().render(g, v);
	}

	/**
	 * sets the controller's mouse observer instance
	 * 
	 * @param mouse
	 *            the mouse observer
	 */
	void setMouse(Mouse mouse) {
		this.mouse = mouse;
	}

	/**
	 * sets the controller's keyboard observer instance
	 * 
	 * @param keyboard
	 *            the keyboard observer
	 */
	void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
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

	/**
	 * performs a game logic update tick.
	 */
	private void tick() {
		this.navigator.tick();
		this.editor.tick();
		this.trackbar.tick();
		this.debugTools.tick();
		// TODO: tick logic
	}

	/**
	 * performs a view tick. This renders the view associated with this
	 * controller.
	 */
	private void viewTick() {
		this.gameView.renderView();
	}

}
