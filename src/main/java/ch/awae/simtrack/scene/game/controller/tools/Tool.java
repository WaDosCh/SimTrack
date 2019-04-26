package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.core.input.InputHandler;

/**
 * describes the basic behaviour of any editor tool. Each editor tool represents a possible editor state.
 */
public interface Tool extends BaseTicker, InputHandler {

	public BaseRenderer getRenderer();

	/**
	 * called when this tool is loaded. If the tool needs to prepare its state this may be done here
	 */
	public default void loadTool(Object... args) {
	}

	public default void unloadTool() {
	}

	@Override
	default void tick() {
	}

}
