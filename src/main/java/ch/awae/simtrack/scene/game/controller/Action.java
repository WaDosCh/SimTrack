package ch.awae.simtrack.scene.game.controller;

import static ch.awae.simtrack.core.Input.MOUSE_LEFT;
import static ch.awae.simtrack.core.Input.MOUSE_RIGHT;
import static java.awt.event.KeyEvent.*;

public enum Action implements ch.awae.simtrack.core.Action {

	// MOVEMENT
	PAN_LEFT(VK_A, VK_LEFT),
	PAN_RIGHT(VK_D, VK_RIGHT),
	PAN_UP(VK_W, VK_UP),
	PAN_DOWN(VK_S, VK_DOWN),

	// BASE TOOLS
	DROP_TOOL(VK_ESCAPE),
	DEBUG_TOOL(VK_F2),

	// BUILD TOOL
	BT_BUILD_TILE(MOUSE_LEFT),
	BT_DELETE_TILE(MOUSE_RIGHT),
	BT_ROTATE_LEFT(VK_Q),
	BT_ROTATE_RIGHT(VK_E),
	BT_MIRROR(VK_TAB),

	// SIGNAL TOOL
	ST_BUILD_SIGNAL(MOUSE_LEFT),
	ST_DELETE_SIGNAL(MOUSE_RIGHT);

	public final int[] keycodes;

	Action(int... keycodes) {
		this.keycodes = keycodes;
	}

	@Override
	public int[] getKeyCodes() {
		return keycodes;
	}
}
