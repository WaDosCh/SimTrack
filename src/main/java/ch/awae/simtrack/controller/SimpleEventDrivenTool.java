package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.renderer.SimpleRenderer;

public abstract class SimpleEventDrivenTool extends RenderingEventDrivenTool implements SimpleRenderer {

	public SimpleEventDrivenTool(Editor editor, UnloadAction action) {
		super(editor, action);
	}

}
