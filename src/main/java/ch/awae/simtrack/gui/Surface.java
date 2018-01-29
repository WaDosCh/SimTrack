package ch.awae.simtrack.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import javax.swing.JPanel;

/**
 * Drawing Surface
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.1
 */
class Surface extends JPanel {

	private static final long serialVersionUID = -6043801963054580971L;

	/**
	 * instantiates a new rendering surface with the given dimensions
	 * 
	 * @param x
	 *            the width in pixels
	 * @param y
	 *            the height in pixels
	 */
	Surface(int x, int y) {
		super();
		this.setSize(x, y);
		this.setMinimumSize(new Dimension(x, y));
		this.setPreferredSize(new Dimension(x, y));
	}

	private Consumer<Graphics2D> renderer = (g) -> {
		// nop
	};

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.renderer.accept((Graphics2D) g);
	}

	/**
	 * set the rendering delegate that forwards the internal paint operation to
	 * the view itself.
	 * 
	 * @param renderer
	 *            the rendering delgate
	 */
	void setRenderingHook(Consumer<Graphics2D> renderer) {
		this.renderer = renderer;
	}

}
