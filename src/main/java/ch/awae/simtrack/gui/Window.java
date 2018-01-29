package ch.awae.simtrack.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Main GUI Window
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.2
 */
class Window extends JFrame {

	private static final long serialVersionUID = 7381994043443871855L;

	private Surface surface;

	/**
	 * instantiates a new main window
	 * 
	 * @param x
	 *            the width of the drawing surface
	 * @param y
	 *            the height of the drawing surface
	 */
	Window(int x, int y) {
		super("SimTrack");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(this.surface = new Surface(x, y));
		this.pack();
		this.setResizable(false);

		// Note: just for debugging / nice to have
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();

		this.setLocation((int) (width / 2 - this.getWidth() / 2), 0);
		this.setFocusTraversalKeysEnabled(false);
		this.setVisible(true);
	}

	/**
	 * retrieves the drawing surface
	 * 
	 * @return the drawing surface
	 */
	Surface getSurface() {
		return this.surface;
	}

}
