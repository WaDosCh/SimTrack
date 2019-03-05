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
		drop = input.getBinding(Action.DROP_TOOL);
		autoUnload = action == GameTool.UnloadAction.UNLOAD;
	}

	@Override
	protected void preTick() {
		super.preTick();
		model = this.scene.getModel();
		mouseScene = this.scene.getViewPort().toSceneCoordinate(mousePosition);
		mouseTile = mouseScene.toTileCoordinate();
	}

	@Override
	public void tick() {
		if (autoUnload && drop.isPressed() && drop.isEdge()) {
			drop.consume();
			editor.loadTool(null);
			logger.debug("auto-unloading tool");
			return;
		}
		super.tick();
	}

}
