package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.view.Design;

public class BasePanel extends BaseComponent {

	private ArrayList<Component> components;
	private boolean needsLayout;
	private boolean centered;
	public int margin = 0;
	private Window window;

	public BasePanel(boolean centered, Window window) {
		this.centered = centered;
		this.window = window;
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
			Dimension size = this.window.getScreenSize();
			layout(margin, margin, size.width - 2 * margin, size.height - Design.toolbarHeight - 2 * margin);
		}

		g.setColor(Design.almostOpaque);
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
		Dimension size = getPreferedDimension();
		if (this.centered) {
			this.pos = new Point(x + w / 2 - size.width / 2, y + h / 2 - size.height / 2);
			this.size = new Dimension(size.width, size.height);
		} else {
			this.pos = new Point(x,y);
			this.size = new Dimension(size.width, size.height);
		}
		int currentY = 0;
		for (Component b : this.components) {
			Dimension componentSize = b.getPreferedDimension();
			b.layout(this.pos.x, currentY + this.pos.y, this.size.width, componentSize.height);
			currentY += componentSize.height;
		}
	}

	@Override
	public Dimension getPreferedDimension() {
		int minWidth = 0;
		int totalHeight = 0;
		for (Component b : this.components) {
			Dimension d = b.getPreferedDimension();
			minWidth = Math.max(minWidth, d.width);
			totalHeight += d.height;
		}
		return new Dimension(minWidth, totalHeight);
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
