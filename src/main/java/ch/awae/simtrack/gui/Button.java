package ch.awae.simtrack.gui;

import java.awt.Graphics2D;
import java.awt.Point;

import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.IGameView;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Button extends Label {

	public final Runnable action;
	public final Mouse mouse;

	public Button(String title, Mouse mouse, Runnable action) {
		super(title);
		this.mouse = mouse;
		this.action = action;
	}

	public void render(Graphics2D g, IGameView view) {
		g.setColor(Design.buttonBackground);
		if (test(this.mouse.getScreenPosition()))
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
		return pos.x >= this.pos.x && pos.y >= this.pos.y
				&& pos.x <= this.pos.x + this.size.getWidth()
				&& pos.y <= this.pos.y + this.size.getHeight();
	}

}