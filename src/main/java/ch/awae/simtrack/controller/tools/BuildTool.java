package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.OnLoad;
import ch.awae.simtrack.controller.OnUnload;
import ch.awae.simtrack.controller.SimpleEventDrivenTool;
import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.tile.FixedTile;
import ch.awae.simtrack.model.tile.Tile;
import ch.awae.simtrack.model.tile.TrackTile;
import ch.awae.simtrack.model.tile.TransformableTrackTile;
import ch.awae.simtrack.model.tile.track.FusedTrackFactory;
import ch.awae.simtrack.model.tile.track.TrackValidator;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.renderer.TrackRenderUtil;
import lombok.Getter;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 */
public class BuildTool extends SimpleEventDrivenTool {

	private static Stroke bullCursorStroke = new BasicStroke(6);
	private static Color darkRed = Color.RED.darker();
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	private @Getter boolean isBulldozeTool;
	private @Getter boolean valid = false;
	private boolean placeGood = false;
	private @Getter TransformableTrackTile track;

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
		ifNot(this::isBulldozeTool).ifPressed(Action.BT_BUILD_TILE, this::place);

	}

	@OnLoad
	public void loadBulldoze() {
		isBulldozeTool = true;
	}

	@OnLoad
	public void loadBuilder(TransformableTrackTile tile) {
		isBulldozeTool = false;
		track = tile;
	}

	@OnUnload
	public void unloadTile() {
		track = null;
	}

	private void checkValid() {
		this.valid = this.isBulldozeTool ? canDelete() : canPlace();
		this.placeGood = !isBulldozeTool && valid && makesPlaceSense();
	}

	private boolean canPlace() {
		if (mouseTile == null)
			return false;
		// in range?
		if (!model.isOnMap(mouseTile))
			return false;

		// compatible?
		Tile tile = model.getTileAt(mouseTile);
		if (tile != null) {
			if (tile instanceof FixedTile)
				return false;
			if (tile instanceof TrackTile) {
				TrackTile ttile = (TrackTile) tile;
				TrackTile fused = FusedTrackFactory.createFusedTrack(ttile, track);
				if (TrackValidator.isValidTrack(fused)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	private boolean makesPlaceSense() {
		if (mouseTile == null)
			return false;
		// in range?
		if (!model.isOnMap(mouseTile))
			return false;

		// compatible?
		Tile tile = model.getTileAt(mouseTile);

		if (tile != null) {
			if (tile instanceof FixedTile)
				return false;
			if (tile instanceof TrackTile) {
				TrackTile ttile = (TrackTile) tile;
				TrackTile fused = FusedTrackFactory.createFusedTrack(ttile, track);
				if (TrackValidator.isValidTrack(fused)) {
					return TrackValidator.intern(ttile) != TrackValidator.intern(fused);
				}
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
		Tile t = model.getTileAt(mouseTile);
		if (t == null || t instanceof FixedTile)
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
		if (canPlace() && makesPlaceSense()) {
			if (input.getMousePosition().y < editor.getController().getGameView().getViewPort()
					.getScreenDimensions().y) {
				if (model.getTileAt(mouseTile) == null)
					model.setTileAt(mouseTile, TrackValidator.intern(track));
				else {
					TrackTile oldTile = (TrackTile) model.getTileAt(mouseTile);
					model.removeTileAt(mouseTile);
					model.setTileAt(mouseTile,
							TrackValidator.intern(FusedTrackFactory.createFusedTrack(oldTile, this.track)));
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
	public void render(Graphics g) {
		TileCoordinate c = mouseTile;
		if (c == null)
			return;
		if (isBulldozeTool) {
			viewPort.focusHex(c, g);
			g.setColor(valid ? Color.RED : darkRed);
			g.setStroke(bullCursorStroke);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g.rotate(angle);
			}
		} else {
			viewPort.focusHex(c, g);
			TrackRenderUtil.renderRails(g, valid ? placeGood ? Color.LIGHT_GRAY : Color.GRAY : Color.RED,
					valid ? placeGood ? Color.GRAY : Color.DARK_GRAY : Color.RED, track.getRailPaths());
		}
	}

}
