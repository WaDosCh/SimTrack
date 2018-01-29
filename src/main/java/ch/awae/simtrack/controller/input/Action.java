package ch.awae.simtrack.controller.input;

import static java.awt.event.KeyEvent.*;
import static ch.awae.simtrack.controller.input.Input.*;

public enum Action {
	// MOVEMENT
	PAN_LEFT(VK_A, VK_LEFT),
	PAN_RIGHT(VK_D, VK_RIGHT),
	PAN_UP(VK_W, VK_UP),
	PAN_DOWN(VK_S, VK_DOWN),
	// BASE TOOLS
	DROP_TOOL(VK_ESCAPE),
	// BUILD TOOL
	BT_BUILD_TILE(MOUSE_LEFT),
	BT_DELETE_TILE(MOUSE_RIGHT),
	BT_ROTATE_LEFT(VK_Q),
	BT_ROTATE_RIGHT(VK_E),
	BT_MIRROR(VK_TAB);

	public final int[] keycodes;
	Action(int... keycodes) {
		this.keycodes = keycodes;
	}
}
