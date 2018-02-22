package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.Model;

public class InGameMenu extends GameTool {

	private InGameMenuRenderer renderer;

	public InGameMenu(Editor<Game> editor) {
		super(editor, GameTool.UnloadAction.UNLOAD);

		this.renderer = new InGameMenuRenderer(input);
		this.renderer.addButton("Resume", this::resume);
		this.renderer.addButton("Save", this::save);
		this.renderer.addButton("Load", this::load);
		this.renderer.addButton("Quit Game", () -> System.exit(0));
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

	private void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("saves/map1.simtrack.save")));
			Model model = (Model) in.readObject();
			in.close();
			this.controller.loadModel(model);
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

	@Override
	public void render(Graphics graphics, Game scene) {
		this.renderer.render(graphics, scene);
	}

}
