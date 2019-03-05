package ch.awae.simtrack.scene.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.ModelFactory;

public class Menu extends Scene<Menu> {

	private BasePanel panel;

	public Menu(Controller controller) {
		super(controller);

		initMenu();

		addRenderer(BaseRenderer.getNamed("Menu", (g, m) -> panel.render(g, m.getWindow())));
	}

	private void initMenu() {
		panel = new BasePanel(input, true);
		panel.add(new Label("Main Menu", true));
		panel.add(new Button("Load Scenario", input, this::loadScenario));
		panel.add(new Button("New Custom Game", input, this::newGame));
		panel.add(new Button("Load Saved Game", input, this::loadGame));
		panel.add(new Button("Options", input, this::openOptions));
		panel.add(new Button("Exit", input, this::exitGame));
	}

	private void loadScenario() {
		logger.info("LOAD SCENARIO");
	}

	private void newGame() {
		logger.info("NEW GAME");
		Model model = ModelFactory.getDefaultModel();
		Game game = new Game(controller, model);
		transitionTo(game);
	}

	private void loadGame() {
		logger.info("LOAD GAME");
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(new File("saves/map1.simtrack.save")));
			Model model = (Model) in.readObject();
			model.load();
			Game game = new Game(controller, model);
			transitionTo(game);
		} catch (
				IOException
				| ClassNotFoundException e) {
			logger.error("error loading save", e);
		}
	}

	private void openOptions() {
		logger.info("OPTIONS");
	}

	private void exitGame() {
		logger.info("EXIT");
		System.exit(0);
	}

}
