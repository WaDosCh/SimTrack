package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.renderer.IRenderer;
import lombok.Getter;

public class SignalBulldozeTool extends EventDrivenTool implements IRenderer {

	private final @Getter IRenderer renderer = this;

	public SignalBulldozeTool(Editor editor) {
		super(editor, UnloadAction.UNLOAD);
	}

	@Override
	public void render(Graphics g, IGameView view) {
		// TODO Auto-generated method stub

	}

}
