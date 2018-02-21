package ch.awae.simtrack.controller;

import ch.awae.simtrack.model.Model;
import ch.awae.simtrack.model.position.SceneCoordinate;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.GameView;
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
