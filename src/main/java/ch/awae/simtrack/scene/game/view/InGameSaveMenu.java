package ch.awae.simtrack.scene.game.view;

import java.io.File;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.saves.SaveGames;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.Spacer;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.model.Model;
import ch.judos.generic.data.StringUtils;

public class InGameSaveMenu extends WindowComponent {

	public static final String CLOSE_ACTION_SAVED = "SAVED";

	private Model model;
	private InputField inputField;
	private DesktopComponent parentUi;

	private SaveGames saveGame;

	public InGameSaveMenu(Model model, InputController input) {
		super(Design.titleFont, input);
		this.model = model;
		this.saveGame = new SaveGames();

		this.title = "Save Game";
		initView();
	}

	private void initView() {
		for (File save : this.saveGame.getAvailableSaves()) {
			addComponent(new Button(save.getName(), input, () -> {
				String name = StringUtils.substr(save.getName(), 0, -".simtrack.save".length());
				this.inputField.setText(name);
				this.input.setFocus(this.inputField);
				this.inputField.focus();
			}));
		}
		addComponent(new Spacer(10, 10));

		this.inputField = new InputField(32, this.input);
		this.inputField.setEnterAction(this::save);
		this.inputField.setText(this.model.lastSaveGameName);
		addComponent(this.inputField);

		BasePanel buttonPanel = new BasePanel().setVertical(false);
		addComponent(buttonPanel);
		buttonPanel.add(new Button("Save", input, this::save));
		buttonPanel.add(new Button("Cancel", input, this::dispose));

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

	private void saveWithName(String name) {
		this.saveGame.saveWithName(name, this.model);
		this.dispose(CLOSE_ACTION_SAVED);
	}

}
