package ch.awae.simtrack.gui.layout;

import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.GameView;
import ch.awae.simtrack.view.renderer.Renderer;

public class Label extends BaseComponent implements Renderer, IComponent {

	public final String title;

	public Label(String title) {
		this(title, false);
	}

	public Label(String title, boolean isTitle) {
		super(isTitle ? Design.titleFont : Design.textFont);
		this.title = title;
		init();
	}

	public void render(Graphics g, GameView view) {
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
