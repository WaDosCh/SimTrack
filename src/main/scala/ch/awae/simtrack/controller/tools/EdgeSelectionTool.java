package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IRenderer;

public class EdgeSelectionTool implements ITool {

	TileCoordinate tc;
	IRenderer renderer;
	Editor editor;

	public EdgeSelectionTool(Editor owner) {
		this.editor = owner;
		this.renderer = new EdgeSelectionToolRenderer(this);
	}

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public String getToolName() {
		return "edgeSel";
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		this.tc = (TileCoordinate) args[0];
	}

	@Override
	public void tick() {
		if (this.editor.getController().getKeyboard().key(KeyEvent.VK_ESCAPE))
			this.editor.loadTool("FreeHand", null);
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}
/*
	private static double preciseDist(double u1, double v1, double u2, double v2) {
		double x = (u1 - u2) + (v1 - v2) * 0.5;
		double y = (v1 - v2) * Math.sqrt(3) / 2;
		return Math.sqrt(x * x + y * y);
	}
*/
}
