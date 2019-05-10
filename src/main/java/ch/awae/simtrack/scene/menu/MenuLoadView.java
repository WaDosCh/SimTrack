package ch.awae.simtrack.scene.menu;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.saves.SaveGames;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Button.Colored;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.Design;

public class MenuLoadView extends WindowComponent {

	private Logger logger = LogManager.getLogger();
	private SceneController sceneController;
	private DesktopComponent parentUi;
	private SaveGames saveGame;

	public MenuLoadView(SceneController controller, InputController input, DesktopComponent parentUi) {
		super(Design.titleFont, input);
		this.sceneController = controller;
		this.parentUi = parentUi;
		this.saveGame = new SaveGames();

		initMenu();
	}

	private void initMenu() {
		this.content = new BasePanel();
		this.title = "Load Game";
		addLoadSaveGameButtons();
		addComponent(new Button("Cancel", this.input, this::dispose));
	}

	private void addLoadSaveGameButtons() {
		for (File savedGame : this.saveGame.getAvailableSaves()) {
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
				this.size = getPreferedDimension();
				layout(this.pos.x, this.pos.y, this.size.width, this.size.height);
				logger.info("delete button clicked for " + savedGame.getName());
			}).setColored(Colored.CAUTION));
			addComponent(savePanel);
		}
	}

	private void loadGame(File savedGame) {
		try {
			Model model = this.saveGame.loadGame(savedGame);
			this.sceneController.loadScene(Game.class, model);
		} catch (Exception e) {
			WindowComponent error = new WindowComponent(Design.titleFont, this.input);
			error.title = "Failed to load savegame";
			error.addComponent(new Label("There was an error loading this savegame."));
			error.addComponent(new Button("Aww Men! :/", this.input, () -> {
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
