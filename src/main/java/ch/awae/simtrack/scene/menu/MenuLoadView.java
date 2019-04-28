package ch.awae.simtrack.scene.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.SceneController;
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

public class MenuLoadView extends WindowComponent {

	private Logger logger = LogManager.getLogger(getClass());
	private SceneController sceneController;
	private DesktopComponent parentUi;

	public MenuLoadView(SceneController controller, InputController input, DesktopComponent parentUi) {
		super(Design.titleFont, input);
		this.sceneController = controller;
		this.parentUi = parentUi;

		initMenu();
	}

	private void initMenu() {
		this.content = new BasePanel();
		this.title = "Load Game";
		addSaveButtons();
		addComponent(new Button("Cancel", this.input, this::dispose));
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
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
				initMenu();
				logger.info("delete button clicked for " + savedGame.getName());
			}));
			addComponent(savePanel);
		}
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
		} catch (Exception e) {
			logger.error("error loading save", e);
			WindowComponent error = new WindowComponent(Design.titleFont, this.input);
			error.title = "Failed to load savegame";
			error.addComponent(new Label("There was an error loading this savegame."));
			error.addComponent(new Button("Aww Men! :/",this.input,()-> {
				error.dispose();
			}));
			this.parentUi.addWindow(error);
		}
	}

	@Override
	public void handleInput(InputEvent event) {
		super.handleInput(event);
		if (!event.isConsumed && event.isPressActionAndConsume(InputAction.DESELECT))
			dispose();
	}

}
