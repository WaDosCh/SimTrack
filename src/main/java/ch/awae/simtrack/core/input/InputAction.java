package ch.awae.simtrack.core.input;

import static ch.awae.simtrack.core.input.InputController.MOUSE_LEFT;
import static ch.awae.simtrack.core.input.InputController.MOUSE_RIGHT;
import static java.awt.event.KeyEvent.*;

public enum InputAction implements InputActionI {

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

	/**
	 * possible keycodes to execute this action
	 */
	public final int[] keycodes;

	InputAction(int... defaultKeycodes) {
		this.keycodes = defaultKeycodes;
	}

	@Override
	public int[] getKeyCodes() {
		return keycodes;
	}
	
	public boolean isActivatedBy(int keyCode) {
		for (int possibleKeyCode : this.keycodes) {
			if (keyCode == possibleKeyCode)
				return true;
		}
		return false;
	}
}
