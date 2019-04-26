package ch.awae.simtrack.scene.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;

public class MenuLoadGame extends Scene {

	private BasePanel panel;

	public MenuLoadGame(Controller controller, InputController input) {
		super(controller);
		initMenu(input);

		addRenderer(panel);
	}

	private void initMenu(InputController input) {
		panel = new BasePanel();
		panel.add(new Label("Load Game", true));
		addSaveButtons(input);
		panel.add(new Button("Cancel", input, this::cancel));
	}

	private void addSaveButtons(InputController input) {
		for (String savedGame : getAvailableSaves()) {
			panel.add(new Button(savedGame, input, () -> {
				loadGame(savedGame);
			}));
		}
	}

	private void cancel() {
		this.sceneController.loadScene(Menu.class);
	}

	private String[] getAvailableSaves() {
		File saveFolder = new File("saves/");
		return saveFolder.list((folder, name) -> name.endsWith(".simtrack.save"));
	}

	private void loadGame(String name) {
		logger.debug("LOAD GAME");
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(new File("saves/" + name)));
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
		panel.handleInput(event);
	}

}
