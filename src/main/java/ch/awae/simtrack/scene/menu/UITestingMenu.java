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
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionH;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionV;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.util.RandomJSExt;
import ch.judos.generic.data.RandomJS;

public class UITestingMenu extends Scene {

	private DesktopComponent ui;
	private int windowNumber = 1;

	public UITestingMenu(Controller controller, Window window, InputController input) {
		super(controller, window);

		this.ui = new DesktopComponent(input);
		this.ui.layout(0, 0, window.getScreenSize().width, window.getScreenSize().height);

		for (int i = 0; i < 10; i++)
			addWindowTest(input);

		addRenderer(this.ui);
	}

	private void addWindowTest(InputController input) {
		WindowComponent window = new WindowComponent(Design.textFont, input);
		window.title = "Move me around " + this.windowNumber++;
		window.isHeadless = RandomJS.getTrueWithProb(10);

		BasePanel panel = window.getContent();
		panel.setVertical(RandomJS.getTrueWithProb(50));
		window.isMovable = RandomJS.getTrueWithProb(90);
		panel.add(new Label("IsMovable: " + window.isMovable, true));
		int nr = this.windowNumber;
		panel.add(new Button("Test Button " + nr, input, () -> {
			System.out.println("Button " + nr + " pressed");
		}));
		panel.add(new InputField(20, input));

		this.ui.addWindow(window,RandomJSExt.getEnum(PositionH.class), RandomJSExt.getEnum(PositionV.class));
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
	}

}
