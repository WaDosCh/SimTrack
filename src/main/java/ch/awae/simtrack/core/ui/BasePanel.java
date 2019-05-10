package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.util.ArrayList;

import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionH;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionV;
import ch.awae.simtrack.scene.game.view.Design;
import ch.awae.simtrack.window.Graphics;
import lombok.Getter;

public class BasePanel extends BaseComponent {

	private ArrayList<Component> components;
	private PositionH positionH;
	private PositionV positionV;
	private Window window;
	private @Getter boolean isVertical;

	public BasePanel() {
		this(PositionH.CENTER, PositionV.CENTER, true);
	}

	public BasePanel(PositionH positionH, PositionV positionV, boolean isVertical) {
		this.positionH = positionH;
		this.positionV = positionV;
		this.isVertical = isVertical;
		this.components = new ArrayList<>();
	}

	public BasePanel setVertical(boolean isVertical) {
		this.isVertical = isVertical;
		return this;
	}

	public BasePanel add(Component... components) {
		for (Component component : components)
			this.components.add(component);
		return this;
	}

	@Override
	public void render(Graphics g) {
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
		this.size = new Dimension(w, h);
		LayoutPositioning layout = new LayoutPositioning(this.positionH, this.positionV, this.size);
		this.pos = layout.getPixelPositionBasedOnEnums(x, y, w, h);
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
