package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Dimension;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.SceneController;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.scene.menu.Menu;
import ch.awae.simtrack.scene.menu.MenuLoadGame;

public class InGameMenu extends GameTool {

	private BasePanel renderer;
	private InputController input;
	private Model model;
	private SceneController sceneController;

	public InGameMenu(Editor editor, InputController input, ViewPortNavigator viewPort, Model model,
			SceneController sceneController) {
		super(editor, viewPort, true);
		this.input = input;
		this.model = model;
		this.sceneController = sceneController;

		this.renderer = new BasePanel();
		this.renderer.add(new Label("Ingame Menu", true));
		this.renderer.add(new Button("Resume", input, this::resume));
		this.renderer.add(new Button("Save", input, this::save));
		this.renderer.add(new Button("Load", input, this::load));
		this.renderer.add(new Button("Quit Game", input, this::quitToMenu));
		this.renderer.add(new Button("Exit Game", input, () -> System.exit(0)));
	}

	@Override
	public void handleInput(InputEvent event) {
		this.renderer.handleInput(event);
		if (!event.isConsumed)
			super.handleInput(event);
	}

	@Override
	public void loadTool(Object... args) {
		this.model.getIsPaused().set(true);
	}

	@Override
	public void unloadTool() {
		this.model.getIsPaused().set(false);
	}

	@Override
	public void render(Graphics graphics) {
		graphics.setColor(Design.menuBlackOverlay);
		Dimension size = this.viewPort.getScreenSize();
		graphics.fillRect(0, 0, size.width, size.height);
		this.renderer.render(graphics);
	}

	private void save() {
		this.editor.loadTool(InGameSaveMenu.class);
	}

	private void quitToMenu() {
		this.sceneController.loadScene(Menu.class);
	}

	private void load() {
		// TODO: check whether current game was saved and confirm dialog to quit?
		this.sceneController.loadScene(MenuLoadGame.class);
	}

	private void resume() {
		this.editor.loadTool(FreeTool.class);
	}

}
