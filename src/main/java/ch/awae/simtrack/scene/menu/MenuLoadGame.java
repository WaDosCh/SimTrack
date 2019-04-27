package ch.awae.simtrack.scene.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.Design;

public class MenuLoadGame extends Scene {

	private Logger logger = LogManager.getLogger(getClass());

	private DesktopComponent ui;
	private WindowComponent window;
	private InputController input;

	public MenuLoadGame(Controller controller, Window window, InputController input) {
		super(controller);
		this.input = input;

		this.ui = new DesktopComponent(input);
		this.ui.layout(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		initMenu();
		addRenderer(this.ui);
	}

	private void initMenu() {
		this.window = new WindowComponent(Design.titleFont, this.input);
		window.title = "Load Game";
		addSaveButtons();
		this.window.addComponent(new Button("Cancel", this.input, this::cancel));
		this.ui.addWindow(this.window);
	}

	private void addSaveButtons() {
		for (File savedGame : getAvailableSaves()) {
			BasePanel savePanel = new BasePanel();
			savePanel.setVertical(false);
			savePanel.add(new Button(savedGame.getName(), this.input, () -> {
				loadGame(savedGame);
			}));
			savePanel.add(new Button("X", this.input, () -> {
				if (!savedGame.delete()) {
					logger.warn("Could not delete savegame: " + savedGame.getName());
				}
				this.window.dispose();
				initMenu();
				logger.info("delete button clicked for " + savedGame.getName());
			}));
			this.window.addComponent(savePanel);
		}
	}

	private void cancel() {
		this.sceneController.loadScene(Menu.class);
	}

	private File[] getAvailableSaves() {
		File saveFolder = new File("saves/");
		return saveFolder.listFiles((folder, name) -> name.endsWith(".simtrack.save"));
	}

	private void loadGame(File savedGame) {
		logger.debug("LOAD GAME");
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(new File("saves/" + savedGame.getName())));
			Model model = (Model) in.readObject();
			this.sceneController.loadScene(Game.class, model);
		} catch (
				IOException
				| ClassNotFoundException e) {
			logger.error("error loading save", e);
		}
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
		if (!event.isConsumed && event.isPressActionAndConsume(InputAction.DESELECT))
			cancel();
	}

}
