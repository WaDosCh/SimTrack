package ch.awae.simtrack.controller;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;

/**
 * Factory for controller instances.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class ControllerFactory {

	/**
	 * creates a new game controller. A game controller manages the in-game
	 * processes.
	 * 
	 * @param model
	 * @param view
	 * @param hooker
	 * @param tps
	 * @param fps
	 * @return a new game controller instantiated with the provided params
	 */
	public static IController buildGameController(IModel model, IGameView view, IGUIControllerHookup hooker, int tps,
			int fps) {
		Input input = new Input(hooker);
		GameController c = new GameController(model, view, hooker, input);
		c.setTPS(tps);
		c.setFPS(fps);
		return c;
	}

}
