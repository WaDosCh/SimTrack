package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;

public class Label extends BaseComponent {

	public final String title;

	public Label(String title) {
		this(title, false);
	}

	public Label(String title, boolean isTitle) {
		super(isTitle ? Design.titleFont : Design.textFont);
		this.title = title;
	}

	public void render(Graphics g) {
		g.setFont(this.font);
		g.setColor(Design.textColor);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	public Dimension getPreferedDimension() {
		Font f = this.font;
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Rectangle2D bounds = f.getStringBounds(getTextForSizeCalculation(), frc);
		return new Dimension((int) bounds.getWidth() + 2 * Design.buttonTextMarginX,
				(int) bounds.getHeight() + 2 * Design.buttonTextMarginY);
	}

	protected String getTextForSizeCalculation() {
		return this.title;
	}

	@Override
	public void handleInput(InputEvent event) {
	}

}
