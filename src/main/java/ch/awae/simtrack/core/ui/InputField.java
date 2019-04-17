package ch.awae.simtrack.core.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;
import ch.judos.generic.data.StringUtils;

public class InputField extends Label {

	private static Logger logger = LogManager.getLogger();
	private int expectedLength;
	private InputController input;
	private String text;
	private boolean focused;

	public InputField(int expectedLength, InputController input) {
		super(null);
		this.input = input;
		this.text = "";
		this.expectedLength = expectedLength;
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
		}
		if (this.focused && event.isPress() && !event.getText().isEmpty()) {
			this.text += event.getText();
			event.consume();
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
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.text, this.pos.x, this.pos.y + this.size.height / 2);
	}

	protected String getTextForSizeCalculation() {
		return StringUtils.repeat("W", this.expectedLength);
	}

}
