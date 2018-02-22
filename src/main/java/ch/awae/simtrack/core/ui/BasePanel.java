package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import ch.awae.simtrack.core.Binding;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.view.Design;

public class BasePanel implements Component {

	private ArrayList<Component> components;
	private Input input;
	private boolean needsLayout;
	private Rectangle rect;
	private Binding click;

	public BasePanel(String title, Input input) {
		this.input = input;
		this.components = new ArrayList<>();
		this.components.add(new Label(title, true));
		this.click = input.getBinding(Input.MOUSE_LEFT);
		this.needsLayout = true;
	}

	public void addButton(String title, Runnable action) {
		this.components.add(new Button(title, input, action));
		this.needsLayout = true;
	}

	@Override
	public void render(Graphics g, Window w) {
		if (this.needsLayout)
			layout(0, 0, w.getCanvasWidth(), w.getCanvasHeight() - Design.toolbarHeight);

		g.setColor(Design.almostOpaque);
		g.draw(this.rect);
		g.setColor(Design.grayBorder);
		g.fill(this.rect);

		for (Component b : this.components) {
			b.render(g, w);
		}

		for (Component b : this.components) {
			b.render(g, w);
		}

		click.onPress(() -> {
			for (Component b : this.components) {
				if (b instanceof Button) {
					Button button = (Button) b;
					if (button.test(input.getMousePosition()))
						button.action.run();
				}
			}
		});
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		// Note: Just centers content inside w/h, if w/h is smaller than
		// required, this does not scale down components
		this.needsLayout = false;
		Dimension size = new Dimension(getPreferedWidth(), getPreferedHeight());
		this.rect = new Rectangle(x + w / 2 - size.width / 2, y + h / 2 - size.height / 2, size.width + 1,
				size.height + 1);
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

}