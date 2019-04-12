package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.menu.Menu;

public class InGameMenu extends GameTool {

	private BasePanel renderer;
	private InputController input;

	public InGameMenu(Editor editor, InputController input) {
		super(editor, true);
		this.input = input;

		this.renderer = new BasePanel(true, this.editor.getScene().getWindow());
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
		this.editor.getScene().getPaused().set(true);
	}

	@Override
	public void unloadTool() {
		this.editor.getScene().getPaused().set(false);
	}

	@Override
	public void render(Graphics graphics) {
		this.renderer.render(graphics);
	}

	private void save() {
		this.editor.loadTool(InGameSaveMenu.class);
	}

	private void quitToMenu() {
		this.editor.getScene().getSceneController().loadScene(Menu.class);
	}

	private void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("saves/map1.simtrack.save")));
			Model model = (Model) in.readObject();
			in.close();
			// TODO: not allowed to switch model in running game, too many
			// issues may arise
			// instead force creation of new game scene.
			// this.scene.loadModel(model);
			this.editor.loadTool(FreeTool.class);
		} catch (
				IOException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void resume() {
		this.editor.loadTool(FreeTool.class);
	}

}
