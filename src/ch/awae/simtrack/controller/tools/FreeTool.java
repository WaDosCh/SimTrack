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
