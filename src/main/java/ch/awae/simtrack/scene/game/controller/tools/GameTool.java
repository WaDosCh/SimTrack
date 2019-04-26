package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.position.SceneCoordinate;

public abstract class GameTool implements Tool, BaseRenderer {

	protected final Logger logger = LogManager.getLogger(getClass());

	protected final Editor editor;
	protected ViewPortNavigator viewPort;

	private final boolean autoUnload;

	public GameTool(Editor editor, ViewPortNavigator viewPort, boolean autoUnloadTool) {
		this.editor = editor;
		this.viewPort = viewPort;
		this.autoUnload = autoUnloadTool;
	}

	protected SceneCoordinate getMouseSceneCoordinate(Point mousePosition) {
		return this.viewPort.toSceneCoordinate(mousePosition);
	}

	@Override
	public void handleInput(InputEvent event) {
		if (autoUnload && event.isPressActionAndConsume(InputAction.DROP_TOOL)) {
			editor.loadTool(FreeTool.class);
			logger.debug("auto-unloading tool");
		}
	}

	@Override
	public BaseRenderer getRenderer() {
		return this;
	}

}
