package ch.awae.simtrack.core.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.view.Design;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxButton extends Label {

	public final Input input;
	private AtomicBoolean dataMapping;

	public CheckboxButton(String title, Input input, AtomicBoolean mapper) {
		super(title);
		this.input = input;
		this.dataMapping = mapper;
	}

	public void render(Graphics g, Window view) {
		g.push();
		Color color = Design.buttonBackground;
		if (test(this.input.getMousePosition()))
			color = Design.buttonHover;
		g.setColor(color);
		g.fillRect(pos.x, pos.y, size.width, size.height);

		color = Design.checkboxNotSelected;
		if (this.dataMapping.get())
			color = Design.checkboxSelected;
		g.setColor(color);
		g.fillRect(pos.x + 5, pos.y + size.height / 2 - 10, 20, 20);
		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width, size.height);
		if (this.dataMapping.get()) {
			g.setStroke(new BasicStroke(3));
			g.drawLine(pos.x + 10, pos.y + size.height / 2, pos.x + 15, pos.y + size.height / 2 + 5);
			g.drawLine(pos.x + 15, pos.y + size.height / 2 + 5, pos.x + 20, pos.y + size.height / 2 - 5);
		}
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.title, 30 + pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
		g.pop();
	}

	@Override
	protected Dimension getPreferedDimension() {
		Dimension baseSize = super.getPreferedDimension();
		baseSize.width += 30;
		return baseSize;
	}

	@Override
	public boolean tryConsume(Point mousePos, int mouseButton) {
		if (mouseButton == 1 && test(mousePos)) {
			this.dataMapping.set(!this.dataMapping.get());
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
