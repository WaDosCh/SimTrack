package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.renderer.IRenderer;

/**
 * describes the basic behaviour of any editor tool. Each editor tool represents
 * a possible editor state.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.0.1
 */
public interface ITool {

	/**
	 * retrieves the renderer for this tool
	 * 
	 * @return the tool renderer
	 */
	public IRenderer getRenderer();

	/**
	 * performs an update tick on the tool
	 */
	void tick();

}
