package ch.awae.simtrack.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.function.Consumer;

/**
 * Drawing Surface
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.1
 */
class Surface extends Canvas {

	private static final long serialVersionUID = -6043801963054580971L;

	private BufferStrategy buffer;

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
		setIgnoreRepaint(true);
	}

	private Consumer<Graphics2D> renderer = (g) -> {
		// nop
	};
	
	void initBuffer() {
		createBufferStrategy(2);
		buffer = getBufferStrategy();
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

	public void doPaint() {
		buffer.show();
		Graphics g = buffer.getDrawGraphics();
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		this.renderer.accept((Graphics2D) g);
		g.dispose();
	}

}
