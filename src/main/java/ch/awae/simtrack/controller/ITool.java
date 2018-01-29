package ch.awae.simtrack.controller;

import ch.awae.simtrack.view.IRenderer;

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
	 * Loads the tool. This method should be used in case a tool requires setup
	 * of other external elements.
	 * 
	 * @param args
	 *            additional parameters, always an array, but might have length
	 *            0.
	 * @throws IllegalStateException
	 *             if the tool cannot be loaded at the moment. The editor will
	 *             fall back to the last tool used.
	 */
	default void load(Object[] args) throws IllegalStateException {
	}

	/**
	 * performs an update tick on the tool
	 */
	void tick();

	/**
	 * Unloads the tool. This method signals the tool that it will be
	 * deactivated. Any external cleanups should occur here. Other than the
	 * {@link #load(Object[])} method, this method cannot deny deactivation.
	 */
	default void onUnload() {
	};

}
