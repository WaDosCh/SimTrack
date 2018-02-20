package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.renderer.Renderer;

/**
 * describes the basic behaviour of any editor tool. Each editor tool represents
 * a possible editor state.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.0.1
 */
public interface Tool {

	/**
	 * retrieves the renderer for this tool
	 * 
	 * @return the tool renderer
	 */
	public Renderer getRenderer();

	/**
	 * performs an update tick on the tool
	 */
	void tick();

}
