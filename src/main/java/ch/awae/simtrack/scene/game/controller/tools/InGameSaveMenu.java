package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Dimension;
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
import ch.awae.simtrack.scene.game.view.Design;

public class InGameSaveMenu extends GameTool {

	private BasePanel panel;
	private Model model;
	private InputController input;
	private InputField inputField;

	public InGameSaveMenu(Editor editor, Model model, InputController input) {
		super(editor, true);
		this.model = model;
		this.input = input;

		this.panel = new BasePanel();
		this.panel.add(new Label("Save game", true));
		this.inputField = new InputField(32, this.input);
		this.inputField.setEnterAction(this::save);
		this.panel.add(this.inputField);
		this.panel.add(new Button("Save with Name", input, this::save));
		this.panel.add(new Button("Quicksave", input, this::quicksave));
		this.panel.add(new Button("Cancel", input, () -> this.editor.loadTool(InGameMenu.class)));
	}
	
	@Override
	public void loadTool(Object... args) {
		this.input.setFocus(this.inputField);
		this.inputField.focus();
		this.editor.getScene().getPaused().set(true);
	}
	
	@Override
	public void unloadTool() {
		this.inputField.unfocus();
		this.input.setFocus(null);
		this.editor.getScene().getPaused().set(false);
	}

	@Override
	public void handleInput(InputEvent event) {
		this.panel.handleInput(event);
		if (!event.isConsumed && event.isPressActionAndConsume(InputAction.DROP_TOOL))
			editor.loadTool(InGameMenu.class);
		else if (!event.isConsumed)
			super.handleInput(event);
	}
	
	private void save() {
		saveWithName(this.inputField.getText());
	}

	private void quicksave() {
		saveWithName("quicksave");
	}
	
	private void saveWithName(String name) {
		try {
			new File("saves/").mkdir();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File("saves/"+name+".simtrack.save")));
			out.writeObject(this.model);
			out.close();
			this.editor.loadTool(FreeTool.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.setColor(Design.menuBlackOverlay);
		Dimension size = this.editor.getScene().getWindow().getScreenSize();
		graphics.fillRect(0, 0, size.width, size.height);
		this.panel.render(graphics);
	}

}
