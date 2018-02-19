package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.controller.OnLoad;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.renderer.IRenderer;
import lombok.Getter;

public class SignalTool extends EventDrivenTool implements IRenderer {

	private @Getter IRenderer renderer = this;
	private boolean isOneWay = false;

	public SignalTool(Editor editor) {
		super(editor, UnloadAction.UNLOAD);

	}
	
	@Override
	public void render(Graphics g, IGameView view) {
		
	}

}
