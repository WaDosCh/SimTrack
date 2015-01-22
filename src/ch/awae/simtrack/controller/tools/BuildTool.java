package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.TilePlacer;
import ch.awae.simtrack.model.RotatableTile;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.renderer.BuildToolRenderer;

public class BuildTool implements ITool {

	private TrackTile t;
	private ARenderer renderer;
	private boolean isBulldoze;

	public BuildTool() {
		this.renderer = new BuildToolRenderer(this);
	}

	public boolean isBulldoze() {
		return this.isBulldoze;
	}

	public TrackTile getTrack() {
		return this.t;
	}

	public boolean isValid() {
		return this.isValid;
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		if (args == null) {
			this.isBulldoze = true;
		} else {
			this.t = (TrackTile) args[0];
			this.isBulldoze = false;
		}
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	private boolean isValid = false;
	private boolean isQ, isE, isTab;

	@Override
	public void tick() {
		if (!this.isBulldoze) {
			// PLACER
			this.t.setPosition(Global.mouse.hexPosition());
			if (Global.keyboard.key(KeyEvent.VK_ESCAPE))
				Global.editor.loadTool("FreeHand", null);
			this.isValid = (TilePlacer.instance().canPlaceOn(this.t
					.getPosition()));
			if (this.isValid && this.t.getPosition() != null) {
				if (Global.mouse.button1()
						&& Global.mouse.position().y < Global.port
								.getScreenDimensions().y) {
					TilePlacer.instance().place(this.t.cloneTrack());
				}
			}
			if (Global.keyboard.key(KeyEvent.VK_Q)) {
				if (this.t instanceof RotatableTile && !this.isQ)
					((RotatableTile) this.t).rotate(false);
				this.isQ = true;
			} else
				this.isQ = false;
			if (Global.keyboard.key(KeyEvent.VK_E)) {
				if (this.t instanceof RotatableTile && !this.isE)
					((RotatableTile) this.t).rotate(true);
				this.isE = true;
			} else
				this.isE = false;
			if (Global.keyboard.key(KeyEvent.VK_TAB)) {
				if (this.t instanceof RotatableTile && !this.isTab)
					((RotatableTile) this.t).mirror();
				this.isTab = true;
			} else
				this.isTab = false;
		} else {
			// BULLDOZE
			TileCoordinate pos = Global.mouse.hexPosition();
			if (pos != null)
				this.isValid = TilePlacer.instance().canRemoveFrom(pos);
			else
				this.isValid = false;
			if (this.isValid) {
				if (Global.mouse.button1()
						&& Global.mouse.position().y < Global.port
								.getScreenDimensions().y) {
					TilePlacer.instance().remove(pos);
				}
			}
		}
	}

	@Override
	public String getToolName() {
		return "Builder";
	}

	@Override
	public ARenderer getRenderer() {
		return this.renderer;
	}

}
