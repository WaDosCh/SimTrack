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
import ch.awae.simtrack.controller.IEditor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Keyboard.KeyTrigger;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.input.Trigger.Direction;
import ch.awae.simtrack.model.position.TileCoordinate;
import lombok.Getter;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FreeTool implements ITool {

	@Getter
	private final FreeToolRenderer renderer;
	private TileCoordinate tile;
	private Mouse mouse;
	private Keyboard keyboard;
	private Editor editor;
	private KeyTrigger esc;

	/**
	 * creates a new tool instance.
	 * 
	 * @param e
	 *            the editor owning the tool
	 */
	public FreeTool(Editor editor) {
		this.renderer = new FreeToolRenderer(this);
		this.editor = editor;
		this.mouse = editor.getController().getMouse();
		this.keyboard = editor.getController().getKeyboard();
		this.esc = this.keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_ESCAPE);
	}

	/**
	 * provides the current tool position
	 * 
	 * @return the current position
	 */
	TileCoordinate getTileCoordinate() {
		return this.tile;
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		// ensure valid position
		this.tile = this.mouse.getTileCoordinate();
	}

	@Override
	public void tick() {
		TileCoordinate tile = this.mouse.getTileCoordinate();
		if (tile != null)
			this.tile = tile;
		if (this.esc.test()) {
			this.editor.loadTool(InGameMenu.class);
		}
	}

}
