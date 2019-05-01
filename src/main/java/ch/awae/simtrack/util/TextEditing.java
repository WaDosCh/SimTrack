package ch.awae.simtrack.util;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.input.InputHandler;
import ch.judos.generic.data.StringUtils;
import lombok.Getter;

public class TextEditing implements InputHandler {
	
	private static final String ALL_TEXT_REGEX = ".";
	private static final String FILE_SAVE_REGEX = "[a-zA-Z0-9-_ +,.&!]";
	private static final String NUMBERS_ONLY_REGEX = "[0-9]";
	
	public String allowedCharactersRegex = ALL_TEXT_REGEX;

	private @Getter String text;
	private @Getter int cursor;

	private int cursorBlinkTimer;

	public TextEditing(String text) {
		this.text = text;
		this.cursor = text.length();
	}
	
	public void setNumbersOnly() {
		this.allowedCharactersRegex = NUMBERS_ONLY_REGEX;
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
		} else if (event.getText().matches(this.allowedCharactersRegex)) {
			this.text = StringUtils.substr(this.text, 0, this.cursor) + event.getText()
					+ StringUtils.substr(this.text, this.cursor);
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

	public void setText(String text) {
		this.text = text;
		this.cursor = this.text.length();
	}

}
