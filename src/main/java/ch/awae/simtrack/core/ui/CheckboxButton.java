package ch.awae.simtrack.core.ui;

import java.awt.Color;
import java.awt.Point;
import java.util.function.Consumer;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.view.Design;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxButton extends Label {

	public final Consumer<Boolean> action;
	public final Input input;
	private boolean selected;

	public CheckboxButton(String title, Input input, boolean selected, Consumer<Boolean> action) {
		super(title);
		this.selected = selected;
		this.input = input;
		this.action = action;
	}

	public void render(Graphics g, Window view) {
		Color color = Design.checkboxNotSelected;
		if (this.selected)
			color = Design.checkboxSelected;
		if (test(this.input.getMousePosition()))
			color = color.brighter();
		g.setColor(color);
		g.fillRect(pos.x, pos.y, size.width, size.height);
		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width, size.height);
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	public boolean tryConsume(InputEvent event) {
		if (test(event.mousePos)) {
			this.selected = !this.selected;
			this.action.accept(this.selected);
			return true;
		}
		return false;
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
