package ch.awae.simtrack.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.scene.game.Game;

public abstract class EventDrivenTool implements Tool, BaseRenderer {

	protected final Editor editor;
	protected final Game scene;
	protected final Logger logger = LogManager.getLogger(getClass());

	public EventDrivenTool(Editor editor) {
		this.editor = editor;
		this.scene = editor.getScene();
	}

	@Override
	public BaseRenderer getRenderer() {
		return this;
	}

}
