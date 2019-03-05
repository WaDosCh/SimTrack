package ch.awae.simtrack.core.ui;

import java.awt.Point;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.view.Design;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Button extends Label {

	public final Runnable action;
	public final Input input;

	public Button(String title, Input input, Runnable action) {
		super(title);
		this.input = input;
		this.action = action;
	}

	public void render(Graphics g, Window view) {
		g.setColor(Design.buttonBackground);
		if (test(this.input.getMousePosition()))
			g.setColor(Design.buttonHover);
		g.fillRect(pos.x, pos.y, size.width, size.height);
		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width, size.height);
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	public boolean tryConsume(Point mousePos, int mouseButton) {
		if (mouseButton == 1 && test(mousePos)) {
			this.action.run();
			return true;
		}
		return false;
	}

}
