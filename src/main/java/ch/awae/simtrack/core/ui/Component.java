package ch.awae.simtrack.core.ui;

import java.awt.Dimension;
import java.awt.Point;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;

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

	/**
	 * @param event
	 * @return true if event is consumed
	 */
	public boolean tryConsume(Point mousePosition, int mouseButton);
}
