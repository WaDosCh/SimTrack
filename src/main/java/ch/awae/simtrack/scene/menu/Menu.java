package ch.awae.simtrack.scene.menu;

import java.awt.Dimension;

import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.WindowComponent;

public class Menu extends Scene {

	private DesktopComponent ui;
	private InputController input;

	public Menu(SceneController controller, Window window, InputController input) {
		super(controller);
		this.input = input;
		initMenu(window);
		addRenderer(this.ui);
	}

	private void initMenu(Window window) {
		this.ui = new DesktopComponent(this.input);
		this.ui.layout(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		openMainMenu();
	}
	
	private void openMainMenu() {
		MainMenuView mainMenu = new MainMenuView(this.input, this.sceneController);
		mainMenu.onClose = (cmd) -> {
			if (MainMenuView.CLOSE_ACTION_LOAD.equals(cmd))
				openLoadView();
			if (MainMenuView.CLOSE_ACTION_NEW_CUSTOM_GAME.equals(cmd))
				openNewCustomGameView();
		};
		this.ui.addWindow(mainMenu);
	}

	private void openNewCustomGameView() {
		WindowComponent customGame = new CustomNewGameView(this.sceneController, this.input);
		customGame.onClose = (cmd) -> {
			openMainMenu();
		};
		this.ui.addWindow(customGame);
	}

	private void openLoadView() {
		WindowComponent loadMenu = new MenuLoadView(this.sceneController, this.input, this.ui);
		loadMenu.onClose = (cmd) -> {
			openMainMenu();
		};
		this.ui.addWindow(loadMenu);
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
	}
	
	@Override
	public void screenResized(Dimension size) {
		this.ui.layout(0, 0, size.width, size.height);
	}

}
