package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.model.Model;

public class InGameSaveMenu extends GameTool {

	private BasePanel panel;
	private Model model;
	private InputController input;

	public InGameSaveMenu(Editor editor, Model model, InputController input) {
		super(editor, true);
		this.model = model;
		this.input = input;

		this.panel = new BasePanel(true, this.scene.getWindow());
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
	public void render(Graphics graphics) {
		this.panel.render(graphics);
	}

}
