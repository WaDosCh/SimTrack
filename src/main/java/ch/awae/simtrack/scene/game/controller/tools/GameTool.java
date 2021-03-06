package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;

public abstract class GameTool implements Tool {

	protected final Logger logger = LogManager.getLogger(getClass());

	protected final Editor editor;
	protected ViewPortNavigator viewPort;

	public GameTool(Editor editor, ViewPortNavigator viewPort) {
		this.editor = editor;
		this.viewPort = viewPort;
	}

	protected SceneCoordinate getMouseSceneCoordinate(Point mousePosition) {
		return this.viewPort.toSceneCoordinate(mousePosition);
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DROP_TOOL)) {
			editor.loadTool(FreeTool.class);
			logger.debug("auto-unloading tool");
		}
	}

}
