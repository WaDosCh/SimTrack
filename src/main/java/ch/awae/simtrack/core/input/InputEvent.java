package ch.awae.simtrack.core.input;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Set;

import javax.annotation.Nonnull;

import lombok.Getter;

/**
 * describes any kind of input that can be done by the user
 */
public class InputEvent {

	public static enum InputEventType {
		PRESS,
		RELEASE,
		CHANGE;
	}
	
	public boolean isConsumed;

	private int keyCode;
	private InputEventType type;
	private Set<Integer> holdKeyCodes;
	private double changeValue;
	private @Nonnull @Getter Point currentMousePosition;

	/**
	 * @param keyCode
	 * @param type
	 * @param changeValue
	 * @param holdKeyCodes general other keyCodes currently hold down
	 * @param currentMousePos
	 */
	public InputEvent(int keyCode, InputEventType type, double changeValue, Set<Integer> holdKeyCodes,
			Point currentMousePos) {
		this.keyCode = keyCode;
		this.type = type;
		this.changeValue = changeValue;
		this.holdKeyCodes = holdKeyCodes;
		this.currentMousePosition = currentMousePos;
		this.isConsumed = false;
	}

	public boolean isPressActionConsumeAndRun(InputAction action, Runnable run) {
		if (isPressActionAndConsume(action)) {
			run.run();
			return true;
		}
		return false;
	}
	
	public boolean isPressActionAndConsume(InputAction action) {
		if (isPressAction(action)) {
			consume();
			return true;
		}
		return false;
	}
	
	public boolean isActionAndConsume(InputAction action) {
		if (isAction(action)) {
			consume();
			return true;
		}
		return false;
	}
	
	public boolean isPressAction(InputAction action) {
		return isPress() && isAction(action);
	}

	public boolean isReleaseAction(InputAction action) {
		return isReleased() && isAction(action);
	}

	public boolean isAction(InputAction action) {
		return action.isActivatedBy(this.keyCode);
	}

	public boolean isPress() {
		return this.type == InputEventType.PRESS;
	}

	public boolean isReleased() {
		return this.type == InputEventType.RELEASE;
	}

	public boolean isChanged() {
		return this.type == InputEventType.CHANGE;
	}

	/**
	 * @return if present any change value (such as mouse scrolling), otherwise 0
	 */
	public double getChangeValue() {
		return this.changeValue;
	}

	public String getText() {
		// if (this.holdKeyCodes.contains(KeyEvent.VK_SHIFT)) {
		// }
		return KeyEvent.getKeyText(this.keyCode);
	}

	/**
	 * is any other action input hold down while this action was generated
	 * 
	 * @param action
	 * @return
	 */
	public boolean isActionHold(InputAction action) {
		for (int possibleKeyCode : action.getKeyCodes()) {
			if (this.holdKeyCodes.contains(possibleKeyCode))
				return true;
		}
		return false;
	}

	public void consume() {
		this.isConsumed = true;
	}

}
