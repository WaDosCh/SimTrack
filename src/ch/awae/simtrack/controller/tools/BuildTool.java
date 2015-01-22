/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.MapManipulator;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.model.RotatableTile;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.SceneViewPort;
import ch.awae.simtrack.view.renderer.BuildToolRenderer;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
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
		// no action required
	}

	private boolean isValid = false;
	private boolean isQ, isE, isTab;

	@Override
	public void tick() {
		if (Keyboard.key(KeyEvent.VK_ESCAPE))
			Editor.loadTool("FreeHand", null);
		if (!this.isBulldoze) {
			// PLACER
			this.t.setPosition(Mouse.hexPosition());
			this.isValid = (MapManipulator.instance().canPlaceOn(this.t
					.getPosition()));
			if (this.isValid && this.t.getPosition() != null) {
				if (Mouse.button1()
						&& Mouse.position().y < SceneViewPort
								.getScreenDimensions().y) {
					MapManipulator.instance().place(this.t.cloneTrack());
				}
			}
			if (Keyboard.key(KeyEvent.VK_Q)) {
				if (this.t instanceof RotatableTile && !this.isQ)
					((RotatableTile) this.t).rotate(false);
				this.isQ = true;
			} else
				this.isQ = false;
			if (Keyboard.key(KeyEvent.VK_E)) {
				if (this.t instanceof RotatableTile && !this.isE)
					((RotatableTile) this.t).rotate(true);
				this.isE = true;
			} else
				this.isE = false;
			if (Keyboard.key(KeyEvent.VK_TAB)) {
				if (this.t instanceof RotatableTile && !this.isTab)
					((RotatableTile) this.t).mirror();
				this.isTab = true;
			} else
				this.isTab = false;
		} else {
			// BULLDOZE
			TileCoordinate pos = Mouse.hexPosition();
			if (pos != null)
				this.isValid = MapManipulator.instance().canRemoveFrom(pos);
			else
				this.isValid = false;
			if (this.isValid) {
				if (Mouse.button1()
						&& Mouse.position().y < SceneViewPort
								.getScreenDimensions().y) {
					MapManipulator.instance().remove(pos);
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
