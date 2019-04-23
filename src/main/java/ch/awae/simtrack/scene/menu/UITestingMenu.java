package ch.awae.simtrack.scene.menu;

import ch.awae.simtrack.core.Controller;
import ch.awae.simtrack.core.Scene;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.DesktopComponent;
import ch.awae.simtrack.core.ui.InputField;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.view.Design;

public class UITestingMenu extends Scene {

	private DesktopComponent ui;
	private int windowNumber = 1;

	public UITestingMenu(Controller controller, Window window, InputController input) {
		super(controller, window);

		this.ui = new DesktopComponent(input);
		this.ui.layout(0, 0, window.getScreenSize().width, window.getScreenSize().height);

		addWindowTest(input);
		addWindowTest(input);

		addRenderer(this.ui);
	}

	private void addWindowTest(InputController input) {
		BasePanel panel = new BasePanel(true, window);
		panel.add(new Label("Test Content Title", true));
		panel.add(new Button("Test Button", input, () -> {
		}));
		panel.add(new InputField(20, input));

		WindowComponent window = new WindowComponent(panel, Design.textFont, "TestWindow " + this.windowNumber++,
				input);
		this.ui.addWindow(window);
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
	}

}
