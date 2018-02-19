package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.SimpleEventDrivenTool;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.view.Graphics;

public class SignalBulldozeTool extends SimpleEventDrivenTool {

	private TileEdgeCoordinate position;

	public SignalBulldozeTool(Editor editor) {
		super(editor, UnloadAction.UNLOAD);
		
		onTick(this::updatePosition);
	}
	
	private void updatePosition() {
		
	}

	@Override
	public void render(Graphics g) {
		
	}

}
