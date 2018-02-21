package ch.awae.simtrack.gui.layout;

import java.awt.Point;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.scene.window.Window;
import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.Graphics;
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

	/**
	 * @param pos
	 * @return true if the position is inside the rectangle of the button
	 */
	public boolean test(Point pos) {
		return pos.x >= this.pos.x && pos.y >= this.pos.y && pos.x <= this.pos.x + this.size.getWidth()
				&& pos.y <= this.pos.y + this.size.getHeight();
	}

}
