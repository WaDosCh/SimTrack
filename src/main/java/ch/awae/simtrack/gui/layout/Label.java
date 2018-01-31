package ch.awae.simtrack.gui.layout;

import java.awt.Graphics2D;

import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.renderer.IRenderer;

public class Label extends BaseComponent implements IRenderer, IComponent {

	public final String title;

	public Label(String title) {
		this(title, false);
	}

	public Label(String title, boolean isTitle) {
		super(isTitle ? Design.titleFont : Design.textFont);
		this.title = title;
		init();
	}

	public void render(Graphics2D g, IGameView view) {
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
