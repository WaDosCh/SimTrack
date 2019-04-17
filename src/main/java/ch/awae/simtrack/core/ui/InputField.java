package ch.awae.simtrack.core.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.util.TextEditing;
import ch.judos.generic.data.StringUtils;

public class InputField extends Label {

	private static Logger logger = LogManager.getLogger();
	private int expectedLength;
	private InputController input;
	private TextEditing text;
	private boolean focused;

	public InputField(int expectedLength, InputController input) {
		super(null);
		this.input = input;
		this.text = new TextEditing("");
		this.expectedLength = expectedLength;
		this.focused = false;
	}

	@Override
	public void unfocus() {
		this.focused = false;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.SELECT)) {
			if (this.test(event.getCurrentMousePosition())) {
				event.consume();
				this.input.setFocus(this);
				this.focused = true;
			} else {
				this.input.unfocus(this);
				this.focused = false;
			}
		} else if (this.focused) {
			this.text.handleInput(event);
		}
	}

	@Override
	public void render(Graphics g, Window w) {
		g.setColor(Design.textFieldBg);
		if (this.focused)
			g.setColor(Design.textFieldFocus);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.textFieldBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		if (this.focused) {
			g.setColor(Design.textFieldBorderFocus);
			g.drawRect(this.pos.x + 1, this.pos.y + 1, this.size.width - 2, this.size.height - 2);
		}
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.text.getTextWithCursor(this.focused), pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	protected String getTextForSizeCalculation() {
		return StringUtils.repeat("W", this.expectedLength);
	}

}
