package ch.awae.simtrack.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;
import lombok.Data;

public @Data class Label implements IRenderer, IComponent {

	public final String title;

	public Point pos;

	private Dimension bounds;
	public Dimension size;

	public Label(String title) {
		this.title = title;
		this.size = new Dimension(getPreferedWidth(), getPreferedHeight());
	}

	public int getPreferedWidth() {
		if (bounds == null)
			loadDimension();
		return bounds.width;
	}

	public int getPreferedHeight() {
		if (bounds == null)
			loadDimension();
		return bounds.height;
	}

	public void render(Graphics2D g, IGameView view) {
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + this.size.height / 2);
	}

	private void loadDimension() {
		Font f = Design.textFont;
		FontRenderContext frc = new FontRenderContext(new AffineTransform(),
				true, true);
		Rectangle2D bounds = f.getStringBounds(this.title, frc);
		this.bounds = new Dimension(
				(int) bounds.getWidth() + 2 * Design.buttonTextMarginX,
				(int) bounds.getHeight() + 2 * Design.buttonTextMarginY);
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		this.pos = new Point(x, y);
		this.size = new Dimension(w, h);
	}

}
