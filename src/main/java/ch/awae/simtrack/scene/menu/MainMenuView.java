package ch.awae.simtrack.scene.menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.scene.uiTest.UITestingMenu;

public class MainMenuView extends WindowComponent {

	public static final String CLOSE_ACTION_LOAD = "LOAD";
	public static final String CLOSE_ACTION_NEW_CUSTOM_GAME = "NEW_CUSTOM_GAME";

	protected final Logger logger = LogManager.getLogger();

	private SceneController sceneController;

	public MainMenuView(InputController input, SceneController sceneController) {
		super(Design.titleFont, input);
		this.sceneController = sceneController;

		this.title = "Main Menu";
		addComponent(new Button("Load Scenario", input, this::loadScenario).setEnabled(false));
		addComponent(new Button("New Custom Game", input, this::newGame));
		addComponent(new Button("Load Game", input, this::loadGame));
		addComponent(new Button("Options", input, this::openOptions).setEnabled(false));
		addComponent(new Button("UI Test Menu", input, this::openTestMenu));
		addComponent(new Button("Exit", input, this::exitGame));
	}

	private void openTestMenu() {
		this.sceneController.loadScene(UITestingMenu.class);
	}

	private void loadScenario() {
		// TODO: implement scenario selection and loading
		logger.debug("LOAD SCENARIO");
	}

	private void newGame() {
		this.dispose(CLOSE_ACTION_NEW_CUSTOM_GAME);
	}

	private void loadGame() {
		this.dispose(CLOSE_ACTION_LOAD);
	}

	private void openOptions() {
		logger.debug("OPTIONS");
	}

	private void exitGame() {
		logger.info("EXIT");
		System.exit(0);
	}

}
