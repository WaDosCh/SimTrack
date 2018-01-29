package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.gui.BasePanel;
import ch.awae.simtrack.view.IRenderer;

public class InGameMenuRenderer extends BasePanel implements IRenderer {

	public InGameMenuRenderer(Mouse mouse) {
		super("Ingame Menu", mouse);
	}

}
