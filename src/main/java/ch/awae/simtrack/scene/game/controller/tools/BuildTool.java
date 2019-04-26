package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.TrackTile;
import ch.awae.simtrack.scene.game.model.tile.TransformableTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.FusedTrackFactory;
import ch.awae.simtrack.scene.game.model.tile.track.TrackValidator;
import ch.awae.simtrack.scene.game.view.renderer.TrackRenderUtil;
import lombok.Getter;

/**
 * Build Tool. Used for placing and deleting track tiles
 * 
 * @author Andreas Wälchli
 */
public class BuildTool extends GameTool {

	private static Stroke bullCursorStroke = new BasicStroke(6);
	private static Color darkRed = Color.RED.darker();
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	private boolean isBulldozeTool;
	private @Getter boolean valid = false;
	private boolean placeGood = false;
	private @Getter TransformableTrackTile track;
	private Model model;
	private InputController input;
	private TileCoordinate mouseTile;

	/**
	 * instantiates a new build tool
	 * 
	 * @param editor the editor the build tool will operate under
	 */
	public BuildTool(Editor editor, Model model, InputController input) {
		super(editor, true);
		this.model = model;
		this.input = input;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.BT_ROTATE_LEFT)) {
			rotateLeft();
			return;
		}
		if (event.isPressActionAndConsume(InputAction.BT_ROTATE_RIGHT)) {
			rotateRight();
			return;
		}
		if (event.isPressActionAndConsume(InputAction.BT_MIRROR)) {
			mirror();
			return;
		}
		if (event.isPressActionAndConsume(InputAction.BT_DELETE_TILE)) {
			bulldoze();
			return;
		}
		if (event.isPressActionAndConsume(InputAction.BT_BUILD_TILE)) {
			if (this.isBulldozeTool)
				bulldoze();
			else
				place();
		}
		super.handleInput(event);
	}

	@Override
	public void tick() {
		Point mousePos = this.input.getMousePosition();
		this.mouseTile = this.getMouseSceneCoordinate(mousePos).toTileCoordinate();
		super.tick();
		checkValid();
	}

	@Override
	public void loadTool(Object... args) {
		if (args.length == 1 && args[0] instanceof TransformableTrackTile) {
			isBulldozeTool = false;
			track = (TransformableTrackTile) args[0];
		} else if (args.length == 0) {
			isBulldozeTool = true;
		} else {
			logger.error("Invalid arguments to load Build Tool: {}", args);
		}
	}

	@Override
	public void unloadTool() {
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
	 * @param c the location to check on
	 * @param m the model to check in
	 * @return {@code true} if and only if the given position in the given model contains a tile and it can be deleted.
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
			if (input.getMousePosition().y < editor.getScene().getViewPort().getScreenSize().height) {
				model.removeTileAt(mouseTile);
			}
		}
	}

	/**
	 * places the tile at the current location or (if applicable) fuses the new tile onto the current one.
	 */
	private void place() {
		if (canPlace() && makesPlaceSense()) {
			if (input.getMousePosition().y < editor.getScene().getViewPort().getScreenSize().height) {
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
		ViewPortNavigator viewPort = this.scene.getViewPort();
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
