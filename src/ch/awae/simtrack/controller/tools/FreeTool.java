package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.renderer.FreeToolRenderer;

public class FreeTool implements ITool {

	private final FreeToolRenderer renderer;
	public TileCoordinate tile;

	public FreeTool() {
		this.renderer = new FreeToolRenderer(this);
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		this.tile = new TileCoordinate(0, 0);
	}

	@Override
	public void unload() {
		// nothing required
	}

	@Override
	public void tick() {
		TileCoordinate tile = Global.mouse.hexPosition();
		if (tile != null)
			this.tile = tile;
		// System.out.println(this.tile);
	}

	@Override
	public String getToolName() {
		return "FreeHand";
	}

	@Override
	public ARenderer getRenderer() {
		return this.renderer;
	}

}
