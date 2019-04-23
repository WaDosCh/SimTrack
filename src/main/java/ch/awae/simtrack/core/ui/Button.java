package ch.awae.simtrack.core.ui;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Button extends Label {

	public final Runnable action;
	public final InputController input;
	private boolean isEnabled;

	public Button(String title, InputController input, Runnable action) {
		super(title);
		this.input = input;
		this.action = action;
		this.isEnabled = true;
	}
	
	public Button setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		return this;
	}

	public void render(Graphics g) {
		g.setColor(Design.buttonBackground);
		if (isPointInside(this.input.getMousePosition()) && this.isEnabled) {
			g.setColor(Design.buttonHover);
		}
		if (!this.isEnabled)
			g.setColor(Design.buttonDisabled);
		g.fillRect(pos.x, pos.y, size.width, size.height);
		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width, size.height);
		g.setFont(Design.textFont);
		if (this.isEnabled)
		g.setColor(Design.textColor);
		else
			g.setColor(Design.textColorDisabled);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.SELECT) && isPointInside(event.getCurrentMousePosition())
				&& this.isEnabled) {
			event.consume();
			this.action.run();
		}
	}

}
