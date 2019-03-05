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

public class InGameMenu extends GameTool {

	private BasePanel renderer;

	public InGameMenu(Editor<Game> editor) {
		super(editor, UnloadAction.UNLOAD);

		this.renderer = new BasePanel(input, true);
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
	public void render(Graphics graphics, Game scene) {
		this.renderer.render(graphics, scene.getWindow());
	}

	private void save() {
		try {
			new File("saves/").mkdir();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("saves/map1.simtrack.save")));
			out.writeObject(this.model);
			out.close();
			this.editor.loadTool(FreeTool.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void quitToMenu() {
		scene.transitionToHome();
	}

	private void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("saves/map1.simtrack.save")));
			Model model = (Model) in.readObject();
			in.close();
			this.scene.loadModel(model);
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
