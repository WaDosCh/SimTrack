package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import lombok.Getter;

public abstract class BaseComponent implements Component {

	protected Dimension size;
	protected Point pos;

	/**
	 * used for panels if more space is available than needed, to decide which components get the space
	 */
	protected @Getter int stretchWeightX = 0;
	protected @Getter int stretchWeightY = 0;

	public BaseComponent() {
		super();
		this.size = new Dimension(0, 0);
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		this.pos = new Point(x, y);
		this.size = new Dimension(w, h);
	}

	public BaseComponent setWeight(int x, int y) {
		this.stretchWeightX = x;
		this.stretchWeightY = y;
		return this;
	}

	/**
	 * @param pos
	 * @return true if the position is inside the rectangle of the button
	 */
	public boolean isPointInside(Point pos) {
		return pos.x >= this.pos.x && pos.y >= this.pos.y && pos.x < this.pos.x + this.size.getWidth()
				&& pos.y < this.pos.y + this.size.getHeight();
	}

	public Dimension measureTextSize(Font font, String text) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Rectangle2D bounds = font.getStringBounds(text, frc);
		return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
	}

}