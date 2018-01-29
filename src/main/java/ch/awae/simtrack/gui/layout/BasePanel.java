package ch.awae.simtrack.gui.layout;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.input.Mouse.MouseTrigger;
import ch.awae.simtrack.controller.input.Trigger.Direction;
import ch.awae.simtrack.view.Design;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;

public class BasePanel implements IRenderer, IComponent {

	private ArrayList<IComponent> components;
	private Mouse mouse;
	private boolean needsLayout;
	private Rectangle rect;
	private MouseTrigger click;

	public BasePanel(String title, Mouse mouse) {
		this.mouse = mouse;
		this.components = new ArrayList<>();
		this.components.add(new Label(title, true));
		this.click = this.mouse.trigger(Direction.ACTIVATE,
				ch.awae.simtrack.controller.input.Mouse.Button.LEFT);
		this.needsLayout = true;
	}

	public void addButton(String title, Runnable action) {
		this.components.add(new Button(title, this.mouse, action));
		this.needsLayout = true;
	}

	@Override
	public void render(Graphics2D g, IGameView view) {
		if (this.needsLayout)
			layout(0, 0, view.getHorizontalScreenSize(),
					view.getVerticalScreenSize() - Design.toolbarHeight);

		g.setColor(Design.almostOpaque);
		g.draw(this.rect);
		g.setColor(Design.grayBorder);
		g.fill(this.rect);

		for (IComponent b : this.components) {
			b.render(g, view);
		}
		if (this.click.test()) {
			for (IComponent b : this.components) {
				if (b instanceof Button) {
					Button button = (Button) b;
					if (button.test(this.mouse.getScreenPosition()))
						button.action.run();
				}
			}
		}
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		// Note: Just centers content inside w/h, if w/h is smaller than
		// required, this does not scale down components
		this.needsLayout = false;
		Dimension size = new Dimension(getPreferedWidth(), getPreferedHeight());
		this.rect = new Rectangle(x + w / 2 - size.width / 2,
				y + h / 2 - size.height / 2, size.width + 1, size.height + 1);
		int currentY = 0;
		for (IComponent b : this.components) {
			Dimension componentSize = b.getSize();
			b.layout(this.rect.x, currentY + this.rect.y, this.rect.width,
					componentSize.height);
			currentY += componentSize.height;
		}
	}

	@Override
	public int getPreferedWidth() {
		int minWidth = 0;
		for (IComponent b : this.components) {
			minWidth = Math.max(minWidth, b.getPreferedWidth());
		}
		return minWidth;
	}

	@Override
	public int getPreferedHeight() {
		int totalHeight = 0;
		for (IComponent b : this.components) {
			totalHeight += b.getPreferedHeight();
		}
		return totalHeight;
	}

	@Override
	public Dimension getSize() {
		return this.rect.getSize();
	}

}
