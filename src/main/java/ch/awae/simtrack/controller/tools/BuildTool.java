package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.model.TileValidator;
import ch.awae.simtrack.model.tile.IFixedTile;
import ch.awae.simtrack.model.tile.ITile;
import ch.awae.simtrack.model.tile.ITrackTile;
import ch.awae.simtrack.model.tile.ITransformableTrackTile;
import ch.awae.simtrack.model.track.FusedTrackFactory;
import ch.awae.simtrack.view.renderer.IRenderer;
import lombok.Getter;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 */
public class BuildTool extends EventDrivenTool {

	private @Getter boolean isBulldozeTool;
	private @Getter boolean valid = false;

	private @Getter IRenderer renderer = new BuildToolRenderer(this);
	private @Getter ITransformableTrackTile track;

	/**
	 * instantiates a new build tool
	 * 
	 * @param editor
	 *            the editor the build tool will operate under
	 */
	public BuildTool(Editor editor) {
		super(editor, UnloadAction.UNLOAD);

		onTick(this::checkValid);

		onPress(Action.BT_ROTATE_LEFT, this::rotateLeft);
		onPress(Action.BT_ROTATE_RIGHT, this::rotateRight);
		onPress(Action.BT_MIRROR, this::mirror);
		
		ifPressed(Action.BT_DELETE_TILE, this::bulldoze);
		ifMet(this::isBulldozeTool).ifPressed(Action.BT_BUILD_TILE, this::bulldoze);
		ifNot(this::isBulldozeTool).ifPressed(Action.BT_BUILD_TILE, this:: place);

	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		if (args.length == 0) {
			this.isBulldozeTool = true;
		} else {
			this.track = (ITransformableTrackTile) args[0];
			this.isBulldozeTool = false;
		}
	}

	private void checkValid() {
		this.valid = this.isBulldozeTool ? canDelete() : canPlace();
	}

	private boolean canPlace() {
		if (mouseTile == null)
			return false;
		ITile tile = model.getTileAt(mouseTile);
		if (tile != null) {
			if (tile instanceof IFixedTile)
				return false;
			if (tile instanceof ITrackTile) {
				ITrackTile ttile = (ITrackTile) tile;
				if (TileValidator.isValidTrack(FusedTrackFactory.createFusedTrack(ttile, track)))
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
	private boolean canDelete() {
		if (mouseTile == null)
			return false;
		ITile t = model.getTileAt(mouseTile);
		if (t == null || t instanceof IFixedTile)
			return false;
		return true;
	}

	private void bulldoze() {
		if (canDelete()) {
			if (input.getMousePosition().y < viewPort.getScreenDimensions().y) {
				model.removeTileAt(mouseTile);
			}
		}
	}

	/**
	 * places the tile at the current location or (if applicable) fuses the new
	 * tile onto the current one.
	 */
	private void place() {
		if (canPlace()) {
			if (input.getMousePosition().y < editor.getController().getGameView().getViewPort()
					.getScreenDimensions().y) {
				if (model.getTileAt(mouseTile) == null)
					model.setTileAt(mouseTile, TileValidator.intern(track));
				else {
					ITrackTile oldTile = (ITrackTile) model.getTileAt(mouseTile);
					model.removeTileAt(mouseTile);
					model.setTileAt(mouseTile,
							TileValidator.intern(FusedTrackFactory.createFusedTrack(oldTile, this.track)));
				}
			}
		}
	}

	private void rotateLeft() {
		if (!isBulldozeTool)
			track = track.rotated(false);
	}

	private void rotateRight() {
		if (!isBulldozeTool)
			track = track.rotated(true);
	}

	private void mirror() {
		if (!isBulldozeTool)
			track = track.mirrored();
	}

}
