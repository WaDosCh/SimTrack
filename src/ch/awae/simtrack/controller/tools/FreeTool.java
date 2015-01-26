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

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IRenderer;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FreeTool implements ITool {

	private final FreeToolRenderer renderer;
	private TileCoordinate tile;
	private Editor owner;

	/**
	 * provides the current tool position
	 * 
	 * @return the current position
	 */
	TileCoordinate getPosition() {
		return this.tile;
	}

	/**
	 * creates a new tool instance.
	 * 
	 * @param e
	 *            the editor owning the tool
	 */
	public FreeTool(Editor e) {
		this.renderer = new FreeToolRenderer(this);
		this.owner = e;
	}

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public String getToolName() {
		return "FreeHand";
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		// ensure valid position
		this.tile = new TileCoordinate(0, 0);
	}

	@Override
	public void tick() {
		TileCoordinate tile = this.owner.getController().getMouse()
				.hexPosition();
		if (tile != null)
			this.tile = tile;
		// TODO: onClick
	}

	@Override
	public void unload() {
		// nothing required
	}

}
