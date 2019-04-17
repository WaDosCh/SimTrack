package ch.awae.simtrack.util;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.input.InputHandler;
import ch.judos.generic.data.StringUtils;
import lombok.Getter;

public class TextEditing implements InputHandler {

	private @Getter String text;
	private @Getter int cursor;

	private int cursorBlinkTimer;

	public TextEditing(String text) {
		this.text = text;
		this.cursor = text.length();
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.BACKSPACE)) {
			if (this.cursor > 0) {
				this.text = StringUtils.substr(this.text, 0, this.cursor - 1)
						+ StringUtils.substr(this.text, this.cursor);
				this.cursor--;
			}
		} else if (event.isPressActionAndConsume(InputAction.DELETE)) {
			if (this.cursor < this.text.length()) {
				this.text = StringUtils.substr(this.text, 0, this.cursor)
						+ StringUtils.substr(this.text, this.cursor + 1);
			}
		} else if (event.isPressActionAndConsume(InputAction.LEFT)) {
			this.cursor--;
			if (this.cursor < 0)
				this.cursor = 0;
		} else if (event.isPressActionAndConsume(InputAction.RIGHT)) {
			this.cursor++;
			if (this.cursor > this.text.length())
				this.cursor = this.text.length();
		} else if (event.isPressActionAndConsume(InputAction.HOME)) {
			this.cursor = 0;
		} else if (event.isPressActionAndConsume(InputAction.END)) {
			this.cursor = this.text.length();
		} else if (!event.getText().isEmpty()) {
			this.text += event.getText();
			this.cursor++;
			event.consume();
		}
	}

	/**
	 * @return call once per frame and the cursor will blink automatically
	 */
	public String getTextWithCursor() {
		return getTextWithCursor(true);
	}

	/**
	 * @return call once per frame and the cursor will blink automatically
	 */
	public String getTextWithCursor(boolean cursorOn) {
		if (!cursorOn)
			return this.text;
		this.cursorBlinkTimer = (this.cursorBlinkTimer + 1) % 60;
		if (this.cursorBlinkTimer < 30) {
			return StringUtils.substr(this.text, 0, this.cursor) + "|" + StringUtils.substr(this.text, this.cursor);
		}
		return StringUtils.substr(this.text, 0, this.cursor) + " " + StringUtils.substr(this.text, this.cursor);
	}

}
