package ch.awae.simtrack.gui.layout;

import java.awt.Dimension;

import ch.awae.simtrack.view.IRenderer;

/**
 * a component is supposed to initialize its base size as the prefered size
 */
public interface IComponent extends IRenderer {

	/**
	 * called by the super component before drawing happens to decide on the
	 * absolute position of the component
	 * 
	 * @param x
	 *            the maximum available space for this component
	 */
	public void layout(int x, int y, int w, int h);

	public int getPreferedWidth();

	public int getPreferedHeight();

	/**
	 * @return
	 */
	public Dimension getSize();
}
