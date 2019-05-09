package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.track.ConstructionTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackValidator;
import ch.awae.simtrack.scene.game.view.renderer.TrackRenderUtil;

/**
 * Build Tool. Used for placing and deleting track tiles
 */
public class BuildTool extends GameTool {

	// XXX: move these properties to a renderer
	private static Stroke bullCursorStroke = new BasicStroke(6);
	private static Color darkRed = Color.RED.darker();

	private boolean isBulldozeTool;
	private boolean valid = false;
	private ConstructionTrackTile track;
	private Model model;
	private InputController input;
	private TileCoordinate mouseTile;

	private enum Action {
		NOTHING,
		PLACE,
		DESTROY;
	}

	private Action currentAction = Action.NOTHING;

	/**
	 * instantiates a new build tool
	 * 
	 * @param editor the editor the build tool will operate under
	 */
	public BuildTool(Editor editor, Model model, InputController input, ViewPortNavigator viewPort) {
		super(editor, viewPort, true);
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
			this.currentAction = Action.DESTROY;
			return;
		}
		if (event.isPressActionAndConsume(InputAction.BT_BUILD_TILE)) {
			this.currentAction = this.isBulldozeTool ? Action.DESTROY : Action.PLACE;
			return;
		}
		if (event.isReleaseAction(InputAction.BT_DELETE_TILE) || event.isReleaseAction(InputAction.BT_BUILD_TILE)) {
			this.currentAction = Action.NOTHING;
			event.consume();
			return;
		}
		super.handleInput(event);
	}

	@Override
	public void tick() {
		if (this.currentAction == Action.PLACE) {
			place();
		} else if (this.currentAction == Action.DESTROY) {
			bulldoze();
		}
	}

	@Override
	public void loadTool(Object... args) {
		if (args.length == 1 && args[0] instanceof TrackTile) {
			isBulldozeTool = false;
			track = (ConstructionTrackTile) args[0];
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
		this.valid = this.isBulldozeTool ? canBulldoze() : canPlace();
	}

	private boolean canPlace() {
		if (mouseTile == null)
			return false;
		if (!model.isOnMap(mouseTile))
			return false;

		Tile tile = model.getTileAt(mouseTile);
		if (this.track.getBuildCost() > this.model.playerMoney)
			return false;

		if (tile == null)
			return true;
		if (tile instanceof FixedTile)
			return false;
		if (tile instanceof TrackTile) {
			TrackTile ttile = (TrackTile) tile;
			ConstructionTrackTile fusedTrack = this.track.fuseWith(ttile);
			return TrackValidator.isValidTrack(fusedTrack) && !fusedTrack.getNormalTrackTile().equals(ttile)
					&& fusedTrack.getBuildCost() <= this.model.playerMoney;
		}
		return false;
	}

	/**
	 * checks if the tile at a given location in a given model can be deleted
	 * 
	 * @param c the location to check on
	 * @param m the model to check in
	 * @return {@code true} if and only if the given position in the given model contains a tile and it can be deleted.
	 */
	private boolean canBulldoze() {
		if (this.mouseTile == null)
			return false;
		Tile t = this.model.getTileAt(this.mouseTile);
		if (t == null || t instanceof FixedTile)
			return false;
		if (this.model.playerMoney < this.model.getBulldozeCost())
			return false;
		return true;
	}

	private void bulldoze() {
		if (canBulldoze()) {
			if (input.getMousePosition().y < this.viewPort.getScreenSize().height) {
				this.model.removeTileAt(this.mouseTile);
				this.model.playerMoney -= this.model.getBulldozeCost();
			}
		}
	}

	/**
	 * places the tile at the current location or (if applicable) fuses the new tile onto the current one.
	 */
	private void place() {
		if (canPlace()) {
			if (model.getTileAt(mouseTile) == null) {
				model.setTileAt(mouseTile, track.getNormalTrackTile());
				model.playerMoney -= track.getBuildCost();
			} else {
				TrackTile oldTile = (TrackTile) model.getTileAt(mouseTile);
				model.removeTileAt(mouseTile);
				ConstructionTrackTile fusedTrack = this.track.fuseWith(oldTile);
				model.playerMoney -= fusedTrack.getBuildCost();
				model.setTileAt(mouseTile, fusedTrack.getNormalTrackTile());
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
		Point mousePos = this.input.getMousePosition();
		this.mouseTile = this.getMouseSceneCoordinate(mousePos).toTileCoordinate();
		checkValid();

		if (this.mouseTile == null)
			return;
		if (isBulldozeTool) {
			this.viewPort.focusHex(this.mouseTile, g);
			g.setColor(valid ? Color.RED : darkRed);
			g.setStroke(bullCursorStroke);
			g.drawHex();
		} else {
			this.viewPort.focusHex(this.mouseTile, g);
			TrackRenderUtil.renderRails(g, valid ? Color.LIGHT_GRAY : Color.RED, valid ? Color.GRAY : Color.RED,
					track.getPaths());
		}
	}

}
