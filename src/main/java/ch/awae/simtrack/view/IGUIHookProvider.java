package ch.awae.simtrack.view;

import java.awt.Graphics2D;
import java.util.function.Consumer;

/**
 * GUI hook required for setting up a view
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public interface IGUIHookProvider {

	/**
	 * provides the rendering delegate. The rendering delegate is a method that
	 * initiates a GUI repaint and thereby a rendering step
	 * 
	 * @return the delegate
	 */
	public Runnable getRenderDelegate();

	/**
	 * hooks a given consumer into the GUI repaint that will be invoked to
	 * render the view
	 * 
	 * @param consumer
	 */
	public void hookComponentRenderer(Consumer<Graphics2D> consumer);

	/**
	 * provides the width of the drawing surface in pixels
	 * 
	 * @return the surface width
	 */
	public int getScreenWidth();

	/**
	 * provides the height of the drawing surface in pixels
	 * 
	 * @return the surface height
	 */
	public int getScreenHeight();

}
