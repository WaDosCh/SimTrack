package ch.awae.simtrack.core.ui;

import java.awt.Dimension;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.core.input.InputHandler;

/**
 * a component is supposed to initialize its base size as the prefered size
 */
public interface Component extends InputHandler {

	/**
	 * called by the super component before drawing happens to decide on the
	 * absolute position of the component
	 * 
	 * @param x
	 *            the maximum available space for this component
	 */
	public void layout(int x, int y, int w, int h);

	public Dimension getPreferedDimension();
	
	/**
	 * @return actual current size this component may use (set by layout method)
	 */
	public Dimension getSize();

	void render(Graphics g, Window w);

}
