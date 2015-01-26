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
import ch.awae.simtrack.model.TileValidator;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.FusedTrackFactory;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IViewPort;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 * @version 1.5, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class BuildTool implements ITool {

	private boolean isBulldoze;
	private boolean isQ, isE, isTab;
	private boolean isValid = false;
	private Editor editor;
	private IRenderer renderer;
	private TileCoordinate pos = null;
	private ITile t;

	/**
	 * instantiates a new build tool
	 * 
	 * @param editor
	 *            the editor the build tool will operate under
	 */
	public BuildTool(Editor editor) {
		this.editor = editor;
		this.renderer = new BuildToolRenderer(this);
	}

	/**
	 * retrieves the current position
	 * 
	 * @return the position
	 */
	TileCoordinate getPosition() {
		return this.pos;
	}

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public String getToolName() {
		return "Builder";
	}

	/**
	 * retrieves the current tile type
	 * 
	 * @return the tile, or {@code null} if the deletion tool is active
	 */
	ITile getTrack() {
		return this.t;
	}

	/**
	 * indicates whether or not the tool is in deletion mode
	 * 
	 * @return {@code true} if and only if deletion mode is active
	 */
	boolean isBulldoze() {
		return this.isBulldoze;
	}

	/**
	 * indicates whether or not the tool is in a valid state.
	 * 
	 * @return {@code true} if and only if the tool is in a valid state.
	 */
	boolean isValid() {
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

	/**
	 * checks if a given tile can be placed at a given location in a given model
	 * 
	 * @param c
	 *            the position to check on
	 * @param m
	 *            the model to check in
	 * @param t
	 *            the tile to check for
	 * @return {@code true} if and only if the tile can either be placed on the
	 *         provided position in the provided model, or if it can be fused
	 *         with the currently present tile.
	 */
	private static boolean canPlaceOn(TileCoordinate c, IModel m, ITile t) {
		if (c == null)
			return false;
		ITile tile = m.getTileAt(c);
		if (tile != null) {
			if (tile.isFixed())
				return false;
			if (TileValidator.isValidTrack(FusedTrackFactory.createFusedTrack(
					tile, t)))
				return true;
			return false;
		}
		return true;
	}

	/**
	 * checks if the tile at a given location in a given model can be deleted
	 * 
	 * @param c
	 *            the location to check on
	 * @param m
	 *            the model to check in
	 * @return {@code true} if and only if the given position in the given model
	 *         contains a tile and it can be deleted.
	 */
	private static boolean canDelete(TileCoordinate c, IModel m) {
		if (c == null)
			return false;
		ITile t = m.getTileAt(c);
		if (t == null || t.isFixed())
			return false;
		return true;
	}

	/**
	 * places the tile at the current location or (if applicable) fuses the new
	 * tile onto the current one.
	 */
	private void place() {
		IModel model = this.editor.getController().getModel();
		if (model.getTileAt(this.pos) == null)
			model.setTileAt(this.pos, this.t.cloneTile());
		else {
			ITile oldTile = model.getTileAt(this.pos);
			model.removeTileAt(this.pos);
			model.setTileAt(this.pos,
					FusedTrackFactory.createFusedTrack(oldTile, this.t));
		}
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
		if (!this.isBulldoze && !mouse.button3()) {
			// PLACER
			this.t.setPosition(this.pos);
			this.isValid = canPlaceOn(this.pos, model, this.t);
			if (this.isValid) {
				if (mouse.button1()
						&& mouse.position().y < port.getScreenDimensions().y) {
					this.place();
				}
			}
			if (keyboard.key(KeyEvent.VK_Q)) {
				if (!this.isQ) {
					this.t.rotate(false);
				}
				this.isQ = true;
			} else
				this.isQ = false;
			if (keyboard.key(KeyEvent.VK_E)) {
				if (!this.isE) {
					this.t.rotate(true);
				}
				this.isE = true;
			} else
				this.isE = false;
			if (keyboard.key(KeyEvent.VK_TAB)) {
				if (!this.isTab) {
					this.t.mirror();
				}
				this.isTab = true;
			} else
				this.isTab = false;
		} else {
			// BULLDOZE
			this.isValid = canDelete(this.pos, model);
			if (this.isValid) {
				if ((mouse.button1() || mouse.button3())
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
