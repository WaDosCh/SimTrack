package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;

public class InGameSaveMenu extends GameTool {

	private BasePanel panel;

	public InGameSaveMenu(Editor<Game> editor) {
		super(editor, UnloadAction.UNLOAD);

		this.panel = new BasePanel(input, true);
		this.panel.add(new Label("Save game", true));
		this.panel.add(new InputField(32, this.input));
		this.panel.add(new Button("Quicksave", input, this::quicksave));
		this.panel.add(new Button("Cancel", input, () -> this.editor.loadTool(InGameMenu.class)));
	}

	private void quicksave() {
		try {
			new File("saves/").mkdir();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File("saves/quicksave.simtrack.save")));
			out.writeObject(this.model);
			out.close();
			this.editor.loadTool(FreeTool.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics graphics, Game scene) {
		this.panel.render(graphics, scene.getWindow());
	}

}
