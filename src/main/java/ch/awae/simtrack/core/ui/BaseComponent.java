package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import lombok.Getter;

public abstract class BaseComponent implements Component {

	@Getter
	public Dimension size;

	protected Point pos;
	protected Font font;

	public BaseComponent(Font font) {
		super();
		this.font = font;
		this.size = new Dimension(0,0);
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		this.pos = new Point(x, y);
		this.size = new Dimension(w, h);
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