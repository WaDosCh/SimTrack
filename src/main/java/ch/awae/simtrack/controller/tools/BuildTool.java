package ch.awae.simtrack.controller.tools;

import java.awt.Graphics2D;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.model.IFixedTile;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.ITrackTile;
import ch.awae.simtrack.model.ITransformableTrackTile;
import ch.awae.simtrack.model.TileValidator;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.FusedTrackFactory;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;
import lombok.Getter;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 */
public class BuildTool extends EventDrivenTool {

	@Getter
	private boolean isBulldozeTool;
	@Getter
	private boolean valid = false;
	private IRenderer renderer = new BuildToolRenderer(this);
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
		super(editor, UnloadAction.UNLOAD);

		onTick(() -> position = viewPort.toHex(input.getMousePosition()));
		onTick(this::checkValid);

		onPress(Action.BT_ROTATE_LEFT, this::rotateLeft);
		onPress(Action.BT_ROTATE_RIGHT, this::rotateRight);
		onPress(Action.BT_MIRROR, this::mirror);

		ifPressed(Action.BT_DELETE_TILE, this::bulldoze);
		ifPressed(Action.BT_BUILD_TILE, () -> {
			if (isBulldozeTool)
				bulldoze();
			else
				place();
		});

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

	private void checkValid() {
		this.valid = this.isBulldozeTool ? canDelete(this.position, controller.getModel())
				: canPlaceOn(this.position, controller.getModel(), this.track);
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
	private static boolean canPlaceOn(TileCoordinate c, IModel m, ITrackTile t) {
		if (c == null)
			return false;
		ITile tile = m.getTileAt(c);
		if (tile != null) {
			if (tile instanceof IFixedTile)
				return false;
			if (tile instanceof ITrackTile) {
				ITrackTile ttile = (ITrackTile) tile;
				if (TileValidator.isValidTrack(FusedTrackFactory.createFusedTrack(ttile, t)))
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

	private void bulldoze() {
		IModel model = this.editor.getController().getModel();
		if (canDelete(this.position, model)) {
			if (input.getMousePosition().y < editor.getController().getGameView().getViewPort()
					.getScreenDimensions().y) {
				model.removeTileAt(this.position);
			}
		}
	}

	/**
	 * places the tile at the current location or (if applicable) fuses the new
	 * tile onto the current one.
	 */
	private void place() {
		IModel model = this.editor.getController().getModel();
		if (canPlaceOn(this.position, model, this.track)) {
			if (input.getMousePosition().y < editor.getController().getGameView().getViewPort()
					.getScreenDimensions().y) {
				if (model.getTileAt(this.position) == null)
					model.setTileAt(this.position, TileValidator.intern(track));
				else {
					ITrackTile oldTile = (ITrackTile) model.getTileAt(this.position);
					model.removeTileAt(this.position);
					model.setTileAt(this.position,
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

	@Override
	public void render(Graphics2D g, IGameView view) {
		renderer.render(g, view);
	}

}
