package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.renderer.Renderer;

public abstract class RenderingEventDrivenTool extends EventDrivenTool implements Renderer {

	public RenderingEventDrivenTool(Editor editor, UnloadAction action) {
		super(editor, action);
	}

	@Override
	public Renderer getRenderer() {
		return this;
	}

}
