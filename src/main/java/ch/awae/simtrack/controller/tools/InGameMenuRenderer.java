package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.gui.layout.BasePanel;
import ch.awae.simtrack.view.IRenderer;

public class InGameMenuRenderer extends BasePanel implements IRenderer {

	public InGameMenuRenderer(Input input) {
		super("Ingame Menu", input);
	}

}
