package ch.awae.simtrack.core;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.ModelFactory;
import ch.awae.simtrack.scene.menu.Menu;

public class SceneFactory {
	protected final Logger logger = LogManager.getLogger();

	private Window window;
	private Controller controller;

	public SceneFactory(Controller controller, Window window) {
		this.controller = controller;
		this.window = window;
	}

	public Scene createScene(Class<? extends Scene> sceneClass, Object... args) {
		HashMap<Class<?>, Object> mapArgs = mapArguments(args);
		if (sceneClass == Game.class) {
			Model model = (Model) mapArgs.get(Model.class);
			if (model == null) {
				logger.info("Creating new model as none was passed to create game scene.");
				model = ModelFactory.getDefaultModel();
			}
			return new Game(controller, model, window);
		} else if (sceneClass == Menu.class) {
			return new Menu(controller, window, controller.getInput());
		} else {
			logger.error("Can't create scene of type {}", sceneClass);
			return null;
		}
	}

	private HashMap<Class<?>, Object> mapArguments(Object[] args) {
		HashMap<Class<?>, Object> result = new HashMap<>();
		for (Object arg : args)
			result.put(arg.getClass(), arg);
		return result;
	}
}
