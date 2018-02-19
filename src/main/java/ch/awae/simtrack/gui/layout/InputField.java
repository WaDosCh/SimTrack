package ch.awae.simtrack.gui.layout;

import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import ch.judos.generic.data.StringUtils;

public class InputField extends BaseComponent {

	private int expectedChar;

	public InputField(int expectedChar) {
		super(Design.textFont);
		this.expectedChar = expectedChar;
	}

	@Override
	public void render(Graphics g, IGameView view) {
		g.setColor(Design.textFieldBg);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.textFieldBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
	}

	@Override
	protected String getTextForSizeCalculation() {
		return StringUtils.repeat("W", this.expectedChar);
	}

}
