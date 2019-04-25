package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Point;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.EventDrivenTool;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;

public abstract class GameTool extends EventDrivenTool {

	private final boolean autoUnload;

	public GameTool(Editor editor, boolean autoUnloadTool) {
		super(editor);
		autoUnload = autoUnloadTool;
	}

	protected SceneCoordinate getMouseSceneCoordinate(Point mousePosition) {
		return this.scene.getViewPort().toSceneCoordinate(mousePosition);
	}

	@Override
	public void handleInput(InputEvent event) {
		if (autoUnload && event.isPressActionAndConsume(InputAction.DROP_TOOL)) {
			editor.loadTool(FreeTool.class);
			logger.debug("auto-unloading tool");
		}
	}

}
