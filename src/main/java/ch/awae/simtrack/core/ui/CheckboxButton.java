package ch.awae.simtrack.core.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.window.Graphics;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckboxButton extends Label {

	public final InputController input;
	private AtomicBoolean dataMapping;

	public CheckboxButton(String title, InputController input, AtomicBoolean mapper) {
		super(title);
		this.input = input;
		this.dataMapping = mapper;
	}

	public void render(Graphics g) {
		g.push();
		Color color = Design.buttonBackground;
		if (isPointInside(this.input.getMousePosition()))
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
	public Dimension getPreferedDimension() {
		Dimension baseSize = super.getPreferedDimension();
		baseSize.width += 30;
		return baseSize;
	}
	
	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.SELECT) && isPointInside(event.getCurrentMousePosition())) {
			event.consume();
			this.dataMapping.set(!this.dataMapping.get());
		}
	}

	/**
	 * @param pos
	 * @return true if the position is inside the rectangle of the button
	 */
	public boolean isPointInside(Point pos) {
		return pos.x >= this.pos.x && pos.y >= this.pos.y && pos.x <= this.pos.x + this.size.getWidth()
				&& pos.y <= this.pos.y + this.size.getHeight();
	}

}
