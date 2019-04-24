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

	public UITestingMenu(Controller controller, Window window, InputController input) {
		super(controller, window);

		this.ui = new DesktopComponent(input);
		this.ui.layout(0, 0, window.getScreenSize().width, window.getScreenSize().height);

		for (int nr = 0; nr < 6; nr++)
			addWindowTest(input, nr);

		addRenderer(this.ui);
	}

	private void addWindowTest(InputController input, int nr) {
		WindowComponent window = new WindowComponent(Design.textFont, input);
		window.title = nr != 0 ? "Move me around " + nr : "fixed window";
		window.isMovable = nr > 0;
		window.isHeadless = RandomJS.getTrueWithProb(10);
		if (nr == 0)window.zIndex = Integer.MAX_VALUE;

		BasePanel panel = window.getContent();
		panel.setVertical(RandomJS.getTrueWithProb(50));
		panel.add(new Label("IsMovable: " + window.isMovable, true));
		panel.add(new Button("Test Button " + nr, input, () -> {
			System.out.println("Button " + nr + " pressed");
		}));
		panel.add(new InputField(20, input));
		panel.add(new Button("Go back", input, () -> {
			this.controller.loadScene(Menu.class);
		}));

		this.ui.addWindow(window, RandomJSExt.getEnum(PositionH.class), RandomJSExt.getEnum(PositionV.class));
	}

	@Override
	public void handleInput(InputEvent event) {
		this.ui.handleInput(event);
	}

}
