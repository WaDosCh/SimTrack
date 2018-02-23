package ch.awae.simtrack.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicBoolean;
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
		this.input = input;
		this.selected = selected;
		this.action = action;
	}

	public CheckboxButton(String title, Input input, AtomicBoolean mapper) {
		this(title, input, mapper.get(), mapper::set);
	}

	public void render(Graphics g, Window view) {
		Color color = Design.buttonBackground;
		if (test(this.input.getMousePosition()))
			color = Design.buttonHover;
		g.setColor(color);
		g.fillRect(pos.x, pos.y, size.width, size.height);

		color = Design.checkboxNotSelected;
		if (this.selected)
			color = Design.checkboxSelected;
		g.setColor(color);
		g.fillRect(pos.x + 5, pos.y + size.height / 2 - 10, 20, 20);

		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width, size.height);
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.title, 30 + pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	protected Dimension getPreferedDimension() {
		Dimension baseSize = super.getPreferedDimension();
		baseSize.width += 30;
		return baseSize;
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
