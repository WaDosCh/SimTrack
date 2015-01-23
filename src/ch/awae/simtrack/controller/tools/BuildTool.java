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
import ch.awae.simtrack.controller.IController;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IViewPort;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class BuildTool implements ITool {

	private boolean isBulldoze;
	private boolean isQ, isE, isTab;
	private boolean isValid = false;

	private Editor editor;

	private IRenderer renderer;
	TileCoordinate pos = null;

	private ITile t;

	public BuildTool(Editor editor) {
		this.editor = editor;
		this.renderer = new BuildToolRenderer(this);
	}

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public String getToolName() {
		return "Builder";
	}

	public ITile getTrack() {
		return this.t;
	}

	public boolean isBulldoze() {
		return this.isBulldoze;
	}

	public boolean isValid() {
		return this.isValid;
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		if (args == null) {
			this.isBulldoze = true;
		} else {
			this.t = (ITile) args[0];
			this.isBulldoze = false;
		}
	}

	private static boolean canPlaceOn(TileCoordinate c, IModel m) {
		if (c == null)
			return false;
		if (m.getTileAt(c) != null)
			return false;
		return true;
	}

	private static boolean canDelete(TileCoordinate c, IModel m) {
		if (c == null)
			return false;
		ITile t = m.getTileAt(c);
		if (t == null || t.isFixed())
			return false;
		return true;
	}

	@Override
	public void tick() {

		IController controller = this.editor.getController();
		IViewPort port = controller.getView().getViewPort();
		IModel model = controller.getModel();
		Mouse mouse = controller.getMouse();
		Keyboard keyboard = controller.getKeyboard();

		this.pos = mouse.hexPosition();

		if (keyboard.key(KeyEvent.VK_ESCAPE)) {
			this.editor.loadTool("FreeHand", null);
			return;
		}
		if (!this.isBulldoze) {
			// PLACER
			this.t.setPosition(this.pos);
			this.isValid = canPlaceOn(this.pos, model);
			if (this.isValid) {
				if (mouse.button1()
						&& mouse.position().y < port.getScreenDimensions().y) {
					model.setTileAt(this.pos, this.t.cloneTile());
				}
			}
			if (keyboard.key(KeyEvent.VK_Q)) {
				if (!this.isQ)
					this.t.rotate(false);
				this.isQ = true;
			} else
				this.isQ = false;
			if (keyboard.key(KeyEvent.VK_E)) {
				if (!this.isE)
					this.t.rotate(true);
				this.isE = true;
			} else
				this.isE = false;
			if (keyboard.key(KeyEvent.VK_TAB)) {
				if (!this.isTab)
					this.t.mirror();
				this.isTab = true;
			} else
				this.isTab = false;
		} else {
			// BULLDOZE
			this.isValid = canDelete(this.pos, model);
			if (this.isValid) {
				if (mouse.button1()
						&& mouse.position().y < port.getScreenDimensions().y) {
					model.removeTileAt(this.pos);
				}
			}
		}
	}

	@Override
	public void unload() {
		// no action required
	}

}
