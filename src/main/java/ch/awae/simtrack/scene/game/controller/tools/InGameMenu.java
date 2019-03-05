package ch.awae.simtrack.scene.game.controller.tools;

import java.io.*;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.OnLoad;
import ch.awae.simtrack.core.OnUnload;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.menu.Menu;

public class InGameMenu extends GameTool {

	private BasePanel<Game> renderer;

	public InGameMenu(Editor<Game> editor) {
		super(editor, true);

		this.renderer = new BasePanel<>(input, true, this.editor.getScene().getWindow());
		this.renderer.add(new Label("Ingame Menu", true));
		this.renderer.add(new Button("Resume", input, this::resume));
		this.renderer.add(new Button("Save", input, this::save));
		this.renderer.add(new Button("Load", input, this::load));
		this.renderer.add(new Button("Quit Game", input, this::quitToMenu));
		this.renderer.add(new Button("Exit Game", input, () -> System.exit(0)));
	}

	@OnLoad
	public void onToolLoad() {
		this.editor.getScene().getPaused().set(true);
	}

	@OnUnload
	public void onToolUnload() {
		this.editor.getScene().getPaused().set(false);
	}

	@Override
	public void render(Graphics graphics) {
		this.renderer.render(graphics, this.scene.getWindow());
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
