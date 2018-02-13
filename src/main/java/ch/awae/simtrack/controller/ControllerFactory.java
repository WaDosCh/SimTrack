package ch.awae.simtrack.controller;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;

/**
 * Factory for controller instances.
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
	public static GameController buildGameController(IModel model, IGameView view, IGUIControllerHookup hooker, int tps,
			int fps) {
		Input input = new Input(hooker);
		return new GameController(model, view, hooker, input);
	}

}
