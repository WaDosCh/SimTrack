package ch.awae.simtrack.scene.game.controller.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.model.Model;

public class InGameSaveMenu extends GameTool {

	private BasePanel panel;
	private Model model;
	private InputController input;
	private InputField inputField;

	public InGameSaveMenu(Editor editor, Model model, InputController input) {
		super(editor, true);
		this.model = model;
		this.input = input;

		this.panel = new BasePanel(true, this.scene.getWindow());
		this.panel.add(new Label("Save game", true));
		this.inputField = new InputField(64, this.input);
		this.panel.add(this.inputField);
		this.panel.add(new Button("Quicksave", input, this::quicksave));
		this.panel.add(new Button("Cancel", input, () -> this.editor.loadTool(InGameMenu.class)));
	}
	
	@Override
	public void loadTool(Object... args) {
		this.input.setFocus(this.inputField);
		this.inputField.focus();
	}
	
	@Override
	public void unloadTool() {
		this.inputField.unfocus();
		this.input.setFocus(null);
	}

	@Override
	public void handleInput(InputEvent event) {
		this.panel.handleInput(event);
		if (!event.isConsumed && event.isPressActionAndConsume(InputAction.DROP_TOOL))
			editor.loadTool(InGameMenu.class);
		else if (!event.isConsumed)
			super.handleInput(event);
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
