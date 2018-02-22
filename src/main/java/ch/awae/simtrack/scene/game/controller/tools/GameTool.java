package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.core.Binding;
import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.EventDrivenTool;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Action;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import lombok.Getter;

public abstract class GameTool extends EventDrivenTool<Game> {

	public enum UnloadAction {
		UNLOAD,
		IGNORE;
	}

	protected @Getter TileCoordinate mouseTile = null;
	protected @Getter SceneCoordinate mouseScene = null;
	protected Model model = null;
	private final Binding drop;
	private final boolean autoUnload;

	public GameTool(Editor<Game> editor, GameTool.UnloadAction action) {
		super(editor);
		model = editor.getScene().getModel();
		drop = input.getBinding(Action.DROP_TOOL);
		autoUnload = action == GameTool.UnloadAction.UNLOAD;
	}

	@Override
	protected void preTick(Game scene) {
		super.preTick(scene);
		mouseScene = scene.getViewPort().toSceneCoordinate(mousePosition);
		mouseTile = mouseScene.toTileCoordinate();
	}

	@Override
	public void tick(Game scene) {
		if (autoUnload && drop.isPressed() && drop.isEdge()) {
			drop.consume();
			editor.loadTool(null);
			logger.info("auto-unloading tool");
			return;
		}
		super.tick(scene);
	}

}
