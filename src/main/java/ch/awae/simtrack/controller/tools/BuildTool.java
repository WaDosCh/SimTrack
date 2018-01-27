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
import ch.awae.simtrack.controller.input.Keyboard.KeyTrigger;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.input.Trigger.Direction;
import ch.awae.simtrack.model.*;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.FusedTrackFactory;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IViewPort;
import lombok.Getter;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 * @version 1.5, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class BuildTool implements ITool {

	@Getter
	private boolean isBulldozeTool;

	private KeyTrigger Q, E, TAB;
	private Keyboard keyboard;
	private Mouse mouse;

	@Getter
	private boolean valid = false;
	private Editor editor;
	@Getter
	private IRenderer renderer;
	@Getter
	private TileCoordinate position = null;
	@Getter
	private ITransformableTrackTile track;

	/**
	 * instantiates a new build tool
	 * 
	 * @param editor
	 *            the editor the build tool will operate under
	 */
	public BuildTool(Editor editor) {
		this.editor = editor;
		mouse = editor.getController().getMouse();
		keyboard = editor.getController().getKeyboard();

		Q = keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_Q);
		E = keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_E);
		TAB = keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_TAB);

		this.renderer = new BuildToolRenderer(this);
	}

	@Override
	public String getToolName() {
		return "Builder";
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		if (args == null) {
			this.isBulldozeTool = true;
		} else {
			this.track = (ITransformableTrackTile) args[0];
			this.isBulldozeTool = false;
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
	private static boolean canPlaceOn(TileCoordinate c, IModel m,
			ITrackTile t) {
		if (c == null)
			return false;
		ITile tile = m.getTileAt(c);
		if (tile != null) {
			if (tile instanceof IFixedTile)
				return false;
			if (tile instanceof ITrackTile) {
				ITrackTile ttile = (ITrackTile) tile;
				if (TileValidator.isValidTrack(
						FusedTrackFactory.createFusedTrack(ttile, t)))
					return true;
			}
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
		if (t == null || t instanceof IFixedTile)
			return false;
		return true;
	}

	/**
	 * places the tile at the current location or (if applicable) fuses the new
	 * tile onto the current one.
	 */
	private void place() {
		IModel model = this.editor.getController().getModel();
		if (model.getTileAt(this.position) == null)
			model.setTileAt(this.position, TileValidator.intern(track));
		else {
			ITrackTile oldTile = (ITrackTile) model.getTileAt(this.position);
			model.removeTileAt(this.position);
			model.setTileAt(this.position, TileValidator.intern(
					FusedTrackFactory.createFusedTrack(oldTile, this.track)));
		}
	}

	@Override
	public void tick() {

		IController controller = this.editor.getController();
		IViewPort port = controller.getGameView().getViewPort();
		IModel model = controller.getModel();

		this.position = mouse.getTileCoordinate();

		if (keyboard.key(KeyEvent.VK_ESCAPE)) {
			this.editor.loadTool("FreeHand", null);
			return;
		}
			// PLACER
			this.valid = canPlaceOn(this.position, model, this.track);
			if (this.valid) {
				if (mouse.button1() && mouse.getScreenPosition().y < port
						.getScreenDimensions().y) {
					this.place();
				}
			}

			Q.test(() -> track = track.rotated(false));
			E.test(() -> track = track.rotated(true));
			TAB.test(() -> track = track.mirrored());

		} else {
			// BULLDOZE
			this.valid = canDelete(this.position, model);
			if (this.valid) {
				if ((mouse.button1() || mouse.button3()) && mouse
						.getScreenPosition().y < port.getScreenDimensions().y) {
					model.removeTileAt(this.position);
				}
			}
		}
	}

}
