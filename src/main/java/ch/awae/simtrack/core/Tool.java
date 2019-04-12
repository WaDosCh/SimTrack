package ch.awae.simtrack.core;

import ch.awae.simtrack.core.input.InputHandler;

/**
 * describes the basic behaviour of any editor tool. Each editor tool represents
 * a possible editor state.
 * 
 * @author Andreas Wälchli
 */
public interface Tool extends BaseTicker, InputHandler {

	/**
	 * retrieves the renderer for this tool
	 * 
	 * @return the tool renderer
	 */
	public BaseRenderer getRenderer();
	
	public default void loadTool(Object... args) {
	}
	
	public default void unloadTool() {
	}
	
	@Override
	default void tick() {
	}

}
