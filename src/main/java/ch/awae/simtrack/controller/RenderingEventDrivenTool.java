package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.renderer.IRenderer;

public abstract class RenderingEventDrivenTool extends EventDrivenTool implements IRenderer {

	public RenderingEventDrivenTool(Editor editor, UnloadAction action) {
		super(editor, action);
	}

	@Override
	public IRenderer getRenderer() {
		return this;
	}

}
