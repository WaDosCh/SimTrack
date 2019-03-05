package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.Binding;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.view.Design;

public class BasePanel implements Component, BaseRenderer {

	private ArrayList<Component> components;
	private Input input;
	private boolean needsLayout;
	private Rectangle rect;
	private Binding click;
	private boolean centered;
	public int margin = 0;
	private Window window;

	public BasePanel(Input input, boolean centered, Window window) {
		this.centered = centered;
		this.input = input;
		this.window = window;
		this.components = new ArrayList<>();
		this.click = input.getBinding(Input.MOUSE_LEFT);
		this.needsLayout = true;
	}

	public void add(Component component) {
		this.components.add(component);
		this.needsLayout = true;
	}
	

	@Override
	public void render(Graphics graphics) {
		render(graphics, this.window);
	}

	public void render(Graphics g, Window w) {

		if (this.needsLayout) {
			Dimension size = this.window.getCanvasSize();
			layout(margin, margin, size.width - 2 * margin, size.height - Design.toolbarHeight - 2 * margin);
		}

		g.setColor(Design.almostOpaque);
		g.fill(this.rect);
		g.setColor(Design.grayBorder);
		g.draw(this.rect);

		for (Component b : this.components) {
			b.render(g, this.window);
		}

		click.onPress(() -> {
			Point pos = this.input.getMousePosition();
			tryConsume(pos, 1);
		});
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		// Note: Just centers content inside w/h, if w/h is smaller than
		// required, this does not scale down components
		this.needsLayout = false;
		Dimension size = new Dimension(getPreferedWidth(), getPreferedHeight());
		if (this.centered) {
			this.rect = new Rectangle(x + w / 2 - size.width / 2, y + h / 2 - size.height / 2, size.width, size.height);
		} else {
			this.rect = new Rectangle(x, y, size.width, size.height);
		}
		int currentY = 0;
		for (Component b : this.components) {
			Dimension componentSize = b.getSize();
			b.layout(this.rect.x, currentY + this.rect.y, this.rect.width, componentSize.height);
			currentY += componentSize.height;
		}
	}

	@Override
	public int getPreferedWidth() {
		int minWidth = 0;
		for (Component b : this.components) {
			minWidth = Math.max(minWidth, b.getPreferedWidth());
		}
		return minWidth;
	}

	@Override
	public int getPreferedHeight() {
		int totalHeight = 0;
		for (Component b : this.components) {
			totalHeight += b.getPreferedHeight();
		}
		return totalHeight;
	}

	@Override
	public Dimension getSize() {
		return this.rect.getSize();
	}

	@Override
	public boolean tryConsume(Point mousePos, int mouseButton) {
		for (Component b : this.components) {
			if (b.tryConsume(mousePos, mouseButton))
				return true;
		}
		return false;
	}

}
