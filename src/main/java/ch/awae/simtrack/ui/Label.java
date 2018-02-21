package ch.awae.simtrack.ui;

import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.Window;
import ch.awae.simtrack.scene.game.view.Design;

public class Label extends BaseComponent {

	public final String title;

	public Label(String title) {
		this(title, false);
	}

	public Label(String title, boolean isTitle) {
		super(isTitle ? Design.titleFont : Design.textFont);
		this.title = title;
		init();
	}

	public void render(Graphics g, Window w) {
		g.setFont(this.font);
		g.setColor(Design.textColor);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	protected String getTextForSizeCalculation() {
		return this.title;
	}

}
