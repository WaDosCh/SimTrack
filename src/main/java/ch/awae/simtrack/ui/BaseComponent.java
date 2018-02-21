package ch.awae.simtrack.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import ch.awae.simtrack.scene.game.view.Design;
import lombok.Getter;

public abstract class BaseComponent implements Component {

	@Getter
	public Dimension size;

	protected Point pos;
	protected Font font;

	private Dimension preferedSize;

	public BaseComponent(Font font) {
		super();
		this.font = font;
	}

	protected void init() {
		this.size = new Dimension(getPreferedWidth(), getPreferedHeight());
	}

	public int getPreferedWidth() {
		if (preferedSize == null)
			loadDimension();
		return preferedSize.width;
	}

	public int getPreferedHeight() {
		if (preferedSize == null)
			loadDimension();
		return preferedSize.height;
	}

	private void loadDimension() {
		Font f = this.font;
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		Rectangle2D bounds = f.getStringBounds(getTextForSizeCalculation(), frc);
		this.preferedSize = new Dimension((int) bounds.getWidth() + 2 * Design.buttonTextMarginX,
				(int) bounds.getHeight() + 2 * Design.buttonTextMarginY);
	}

	protected abstract String getTextForSizeCalculation();

	@Override
	public void layout(int x, int y, int w, int h) {
		this.pos = new Point(x, y);
		this.size = new Dimension(w, h);
	}

}