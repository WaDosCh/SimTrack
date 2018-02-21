package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.view.GameView;
import lombok.Getter;

public abstract class GameTool extends EventDrivenTool<GameView> {

	public GameTool(Editor<GameView> editor, EventDrivenTool.UnloadAction action) {
		super(editor, action);
		model = editor.getController().getModel();
	}

	protected @Getter TileCoordinate mouseTile = null;
	protected @Getter SceneCoordinate mouseScene = null;
	protected Model model = null;

	@Override
	protected void preTick(GameView scene) {
		super.preTick(scene);
		mouseScene = scene.getViewPort().toSceneCoordinate(mousePosition);
		mouseTile = mouseScene.toTileCoordinate();
	}

}
