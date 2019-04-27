package ch.awae.simtrack.scene.menu;

import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.view.Design;

public class Menu extends Scene {

	private DesktopComponent ui;

	public Menu(SceneController controller, Window window, InputController input) {
		super(controller);
		initMenu(input, window);
		addRenderer(this.ui);
	}

	private void initMenu(InputController input, Window window) {
		this.ui = new DesktopComponent(input);
		this.ui.layout(0, 0, window.getScreenSize().width, window.getScreenSize().height);

		WindowComponent win = new WindowComponent(Design.titleFont, input);
		win.title = "Main Menu";
		win.addComponent(new Button("Load Scenario", input, this::loadScenario).setEnabled(false));
		win.addComponent(new Button("New Custom Game", input, this::newGame));
		win.addComponent(new Button("Load Saved Game", input, this::loadGame));
		win.addComponent(new Button("Options", input, this::openOptions).setEnabled(false));
		win.addComponent(new Button("UI Test Menu", input, this::openTestMenu));
		win.addComponent(new Button("Exit", input, this::exitGame));
		this.ui.addWindow(win);
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
		this.sceneController.loadScene(Game.class);
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
		this.ui.handleInput(event);
	}

}
