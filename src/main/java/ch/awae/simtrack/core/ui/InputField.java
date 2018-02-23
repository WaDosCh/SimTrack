package ch.awae.simtrack.core.ui;

import java.awt.Dimension;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.view.Design;
import ch.judos.generic.data.StringUtils;

public class InputField extends BaseComponent {

	private int expectedChar;

	public InputField(int expectedChar) {
		super(Design.textFont);
		this.expectedChar = expectedChar;
	}

	@Override
	public void render(Graphics g, Window w) {
		g.setColor(Design.textFieldBg);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.textFieldBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
	}

	protected String getTextForSizeCalculation() {
		return StringUtils.repeat("W", this.expectedChar);
	}

	@Override
	public boolean tryConsume(InputEvent event) {
		return false;
	}

	@Override
	protected Dimension getPreferedDimension() {

		return null;
	}

}
