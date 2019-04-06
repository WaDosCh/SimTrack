package ch.awae.simtrack.core.ui;

import java.awt.Point;
import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.view.Design;
import ch.judos.generic.data.StringUtils;

public class InputField extends Label {

	private static Logger logger = LogManager.getLogger();
	private int expectedLength;
	private InputController input;
	private boolean focused;
	private String text;

	public InputField(int expectedLength, InputController input) {
		super(null);
		this.input = input;
		this.text = "";
		this.expectedLength = expectedLength;

		input.getBinding(KeyEvent.VK_A).ifPressed(() -> {
			this.text += "A";
			logger.info("text is now: {}", this.text);
			//TODO: input doesn't work
		});
	}

	@Override
	public void render(Graphics g, Window w) {
		g.setColor(Design.textFieldBg);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.textFieldBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.text, this.pos.x, this.pos.y);
	}

	protected String getTextForSizeCalculation() {
		return StringUtils.repeat("W", this.expectedLength);
	}

	@Override
	public boolean tryConsume(Point mousePos, int mouseButton) {
		if (mouseButton == 1 && test(mousePos)) {
			this.focused = true;
			return true;
		}
		this.focused = false;
		return false;
	}

}
