package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;
import lombok.Setter;

public class BasePanel extends BaseComponent {

	public static enum PositionH {
		LEFT,
		CENTER,
		RIGHT;
	}

	public static enum PositionV {
		TOP,
		CENTER,
		BOTTOM;
	}

	private ArrayList<Component> components;
	private boolean needsLayout;
	private PositionH positionH;
	private PositionV positionV;
	public int margin = 0;
	private Window window;
	private @Setter boolean isVertical;

	public BasePanel() {
		this(PositionH.CENTER, PositionV.CENTER, true);
	}

	public BasePanel(PositionH positionH, PositionV positionV, boolean isVertical) {
		this.positionH = positionH;
		this.positionV = positionV;
		this.isVertical = isVertical;
		this.components = new ArrayList<>();
		this.needsLayout = true;
	}

	public void add(Component component) {
		this.components.add(component);
		this.needsLayout = true;
	}

	@Override
	public void render(Graphics g) {
		if (this.needsLayout) {
			Dimension size = g.getClipBounds().getSize();
			layout(margin, margin, size.width - 2 * margin, size.height - Design.toolbarHeight - 2 * margin);
		}

		g.setColor(Design.panelBackground);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.grayBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);

		for (Component b : this.components) {
			b.render(g);
		}
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		// Note: Just centers content inside w/h, if w/h is smaller than
		// required, this does not scale down components
		this.needsLayout = false;
		this.size = getPreferedDimension();
		this.pos = getPixelPositionBasedOnEnums(x, y, w, h);
		int currentOffset = 0;
		for (Component b : this.components) {
			Dimension componentSize = b.getPreferedDimension();
			if (this.isVertical) {
				b.layout(this.pos.x, this.pos.y + currentOffset, this.size.width, componentSize.height);
				currentOffset += componentSize.height;
			} else {
				b.layout(this.pos.x + currentOffset, this.pos.y, componentSize.width, this.size.height);
				currentOffset += componentSize.width;
			}
		}
	}

	private Point getPixelPositionBasedOnEnums(int x, int y, int w, int h) {
		int px, py;
		if (this.positionH == PositionH.LEFT)
			px = x;
		else if (this.positionH == PositionH.CENTER)
			px = x + w / 2 - this.size.width / 2;
		else
			px = x + w - this.size.width;
		if (this.positionV == PositionV.TOP)
			py = y;
		else if (this.positionV == PositionV.CENTER)
			py = y + h / 2 - this.size.height / 2;
		else
			py = y + h - this.size.height;
		return new Point(px, py);
	}

	@Override
	public Dimension getPreferedDimension() {
		int width = 0;
		int height = 0;
		for (Component b : this.components) {
			Dimension d = b.getPreferedDimension();
			if (this.isVertical) {
				width = Math.max(width, d.width);
				height += d.height;
			} else {
				width += d.width;
				height = Math.max(height, d.height);
			}
		}
		return new Dimension(width, height);
	}

	@Override
	public void handleInput(InputEvent event) {
		for (Component b : this.components) {
			b.handleInput(event);
			if (event.isConsumed)
				return;
		}
	}

}
