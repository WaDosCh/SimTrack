package ch.awae.simtrack.controller;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IView;

public class ControllerFactory {

	public static IController buildGameController(IModel model, IView view,
			IGUIControllerHookup hooker, int tps, int fps) {
		GameController c = new GameController(model, view);
		c.setTPS(tps);
		c.setFPS(fps);
		c.setMouse(new Mouse(c, hooker));
		c.setKeyboard(new Keyboard(hooker));
		return c;
	}

}
