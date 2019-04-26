package ch.awae.simtrack.scene.menu;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.ModelFactory;

public class Menu extends Scene {

	private BasePanel panel;

	public Menu(Controller controller, InputController input) {
		super(controller);
		initMenu(input);
		addRenderer(panel);
	}

	private void initMenu(InputController input) {
		panel = new BasePanel();
		panel.add(new Label("Main Menu", true));
		panel.add(new Button("Load Scenario", input, this::loadScenario));
		panel.add(new Button("New Custom Game", input, this::newGame));
		panel.add(new Button("Load Saved Game", input, this::loadGame));
		panel.add(new Button("Options", input, this::openOptions).setEnabled(false));
		panel.add(new Button("UI Test Menu", input, this::openTestMenu));
		panel.add(new Button("Exit", input, this::exitGame));
	}

	private void openTestMenu() {
		this.sceneController.loadScene(UITestingMenu.class);
	}

	private void loadScenario() {
		// TODO: implement scenario selection and loading
		logger.debug("LOAD SCENARIO");
	}

	private void newGame() {
		logger.debug("NEW GAME");
		Model model = ModelFactory.getDefaultModel();
		this.sceneController.loadScene(Game.class, model);
	}

	private void loadGame() {
		logger.debug("LOAD GAME");
		this.sceneController.loadScene(MenuLoadGame.class);
	}

	private void openOptions() {
		logger.debug("OPTIONS");
	}

	private void exitGame() {
		logger.info("EXIT");
		System.exit(0);
	}

	@Override
	public void handleInput(InputEvent event) {
		panel.handleInput(event);
	}

}
