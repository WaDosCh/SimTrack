package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;

/**
 * organize multiple WindowComponents, handling correct draw & input order, also responsible for moving and alwaysOnTop
 * property
 */
public class DesktopComponent extends BaseComponent {

	/**
	 * sorted by z-index. Last window will be on top of everything (getting input first)
	 */
	private ArrayList<WindowComponent> windows;
	private InputController input;

	public DesktopComponent(InputController input) {
		super();
		this.input = input;
		this.windows = new ArrayList<WindowComponent>();
	}

	public void addWindow(WindowComponent window) {
		this.windows.add(window);

		// center window on this desktop
		Dimension windowSize = window.getPreferedDimension();
		window.layout(this.pos.x + this.size.width / 2 - windowSize.width / 2,
				this.pos.y + this.size.height / 2 - windowSize.height / 2, windowSize.width, windowSize.height);
	}

	@Override
	public void render(Graphics g) {
		int activateHoveringAt = 0;
		// traverse windows in reverse to check which windows should be able to show hovering on components
		for (int i = this.windows.size() - 1; i >= 0; i--) {
			WindowComponent window = this.windows.get(i);
			if (window.isPointInside(this.input.getMousePosition())) {
				activateHoveringAt = i;
				break;
			}
		}
		
		Collections.sort(this.windows, (win1, win2) -> win1.zIndex - win2.zIndex);
		int zIndex = 0;
		this.input.setHoverEnabled(false);
		for (int i = 0; i<this.windows.size(); i++) {
			if (i == activateHoveringAt)
				this.input.setHoverEnabled(true);
			WindowComponent window = this.windows.get(i);
			window.zIndex = zIndex;
			window.render(g);
			zIndex++;
		}
		this.input.setHoverEnabled(true);
	}

	@Override
	public void handleInput(InputEvent event) {
		// last window is on top, thus receiving input first
		for (int i = this.windows.size() - 1; i >= 0; i--) {
			WindowComponent window = this.windows.get(i);
			window.handleInput(event);
			if (event.isConsumed)
				break;
		}
	}

	/**
	 * does Desktop Component does not have a prefered size
	 */
	@Override
	public Dimension getPreferedDimension() {
		return new Dimension(0, 0);
	}

}
