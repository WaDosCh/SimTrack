package ch.awae.simtrack.gui.layout;

import java.awt.Dimension;

import ch.awae.simtrack.scene.window.Window;
import ch.awae.simtrack.view.Graphics;

/**
 * a component is supposed to initialize its base size as the prefered size
 */
public interface Component {

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
	
	void render(Graphics g, Window w);
}
