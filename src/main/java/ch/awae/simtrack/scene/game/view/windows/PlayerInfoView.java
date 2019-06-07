package ch.awae.simtrack.scene.game.view.windows;

import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.ui.LabelWithUpdatedText;
import ch.awae.simtrack.core.ui.WindowComponent;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.Design;

public class PlayerInfoView extends WindowComponent {

	private Model model;

	public PlayerInfoView(Model model, InputController input) {
		super(Design.textFont, input);
		this.model = model;
		initView();
	}

	private void initView() {
		this.isHeadless = false;
		this.title = "Player information";
		addComponent(new LabelWithUpdatedText(()-> model.playerMoney+"$", false).setWeight(1, 0));
	}

}
