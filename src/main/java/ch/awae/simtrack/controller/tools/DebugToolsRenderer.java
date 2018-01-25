package ch.awae.simtrack.controller.tools;

import java.awt.Graphics2D;

import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IView;

public class DebugToolsRenderer implements IRenderer {

	private DebugTools debugTools;

	public DebugToolsRenderer(DebugTools debugTools) {
		this.debugTools = debugTools;
	}

	@Override
	public void render(Graphics2D g, IView view) {
	}

}
