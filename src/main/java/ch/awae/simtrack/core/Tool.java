package ch.awae.simtrack.core;

/**
 * describes the basic behaviour of any editor tool. Each editor tool represents
 * a possible editor state.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.0.1
 */
public interface Tool extends BaseTicker {

	/**
	 * retrieves the renderer for this tool
	 * 
	 * @return the tool renderer
	 */
	public BaseRenderer getRenderer();

}
