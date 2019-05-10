package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionH;
import ch.awae.simtrack.core.ui.LayoutPositioning.PositionV;
import ch.awae.simtrack.window.Graphics;

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
		addWindow(window, PositionH.CENTER, PositionV.CENTER);
	}
	
	public void addWindow(WindowComponent window, PositionH posH, PositionV posV) {
		this.windows.add(window);
		
		Dimension windowSize = window.getPreferedDimension();
		LayoutPositioning layout = new LayoutPositioning(posH,posV, windowSize);
		Point pos = layout.getPixelPositionBasedOnEnums(this.pos.x, this.pos.y, this.size.width, this.size.height);
		
		window.layout(pos.x, pos.y, windowSize.width, windowSize.height);
	}

	@Override
	public void render(Graphics g) {
		int activateHoveringAt = 0;
		
		Iterator<WindowComponent> it = this.windows.iterator();
		while (it.hasNext()) {
			WindowComponent window = it.next();
			if (window.isDisposed)
				it.remove();
		}
		
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
