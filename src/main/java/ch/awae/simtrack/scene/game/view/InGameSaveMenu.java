package ch.awae.simtrack.scene.game.view;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.model.Model;

public class InGameSaveMenu extends WindowComponent {

	public static final String CLOSE_ACTION_SAVED = "SAVED";

	private Model model;
	private InputField inputField;
	private DesktopComponent parentUi;

	public InGameSaveMenu(Model model, InputController input) {
		super(Design.titleFont, input);
		this.model = model;

		this.title = "Save Game";
		this.inputField = new InputField(32, this.input);
		this.inputField.setEnterAction(this::save);
		addComponent(this.inputField);
		addComponent(new Button("Save with Name", input, this::save));
		addComponent(new Button("Quicksave", input, this::quicksave));
		addComponent(new Button("Cancel", input, this::dispose));

		this.input.setFocus(this.inputField);
		this.inputField.focus();
	}

	@Override
	public void dispose(String closeCommand) {
		this.inputField.unfocus();
		this.input.setFocus(null);
		super.dispose(closeCommand);
	}

	@Override
	public void handleInput(InputEvent event) {
		super.handleInput(event);
		if (!event.isConsumed && event.isPressActionAndConsume(InputAction.DROP_TOOL))
			this.dispose();

		// does not pass events to background / blocking dialog
		event.consume();
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
					new FileOutputStream(new File("saves/" + name + ".simtrack.save")));
			out.writeObject(this.model);
			out.close();
			this.dispose(CLOSE_ACTION_SAVED);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics g) {
		if (!this.isVisible)
			return;
		g.setColor(Design.menuBlackOverlay);
		Dimension size = g.getClipBounds().getSize();
		g.fillRect(0, 0, size.width, size.height);
		super.render(g);
	}

}
