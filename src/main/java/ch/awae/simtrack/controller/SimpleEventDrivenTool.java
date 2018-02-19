package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.renderer.ISimpleRenderer;

public abstract class SimpleEventDrivenTool extends RenderingEventDrivenTool implements ISimpleRenderer {

	public SimpleEventDrivenTool(Editor editor, UnloadAction action) {
		super(editor, action);
	}

}
