package ch.awae.simtrack.scene.game.view;

import java.awt.Rectangle;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.menu.Menu;
import ch.awae.simtrack.scene.menu.MenuLoadView;

public class InGameMenu extends WindowComponent {

	private Model model;
	private SceneController sceneController;
	private Editor editor;
	private DesktopComponent parentUi;

	public InGameMenu(InputController input, Model model, SceneController sceneController, DesktopComponent ui) {
		super(Design.titleFont, input);
		this.model = model;
		this.sceneController = sceneController;
		this.parentUi = ui;
		
		this.model.getIsPaused().set(true);

		this.title = "Game Menu";
		addComponent(new Button("Resume", input, this::dispose));
		addComponent(new Button("Save", input, this::save));
		addComponent(new Button("Load", input, this::load));
		addComponent(new Button("Quit Map", input, this::quitToMenu));
		addComponent(new Button("Exit to Desktop", input, () -> System.exit(0)));
	}
	
	@Override
	public void dispose() {
		this.model.getIsPaused().set(false);
		super.dispose();
	}
	
	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DROP_TOOL)) {
			this.dispose();
		}
		if (!event.isConsumed)
			super.handleInput(event);
		// does not pass events to background / blocking dialog
		event.consume();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Design.menuBlackOverlay);
		Rectangle bounds = g.getClipBounds();
		g.fillRect(0, 0, bounds.width, bounds.height);
		super.render(g);
	}

	private void save() {
		this.isVisible = false;
		InGameSaveMenu saveWindows = new InGameSaveMenu(this.model, this.input);
		saveWindows.onClose = (cmd) -> {
			if (InGameSaveMenu.CLOSE_ACTION_SAVED.equals(cmd)) {
				dispose();
			}
			else {
				this.isVisible = true;
			}
		};
		this.parentUi.addWindow(saveWindows);
	}
	
	private void load() {
		this.isVisible = false;
		MenuLoadView loadWindow = new MenuLoadView(this.sceneController, this.input, this.parentUi);
		loadWindow.onClose = (cmd) -> {
			this.isVisible = true;
		};
		this.parentUi.addWindow(loadWindow);
	}

	private void quitToMenu() {
		this.sceneController.loadScene(Menu.class);
	}

}
