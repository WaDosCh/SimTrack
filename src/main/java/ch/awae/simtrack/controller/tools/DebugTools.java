package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.Editor;
import lombok.Getter;

public class DebugTools {

	private Editor editor;
	@Getter
	private DebugToolsRenderer renderer;

	public DebugTools(Editor editor) {
		this.editor = editor;
		this.renderer = new DebugToolsRenderer(this);
	}

	public void tick() {
	}

}
