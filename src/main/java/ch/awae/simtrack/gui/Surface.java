package ch.awae.simtrack.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.function.Consumer;

import ch.awae.simtrack.view.Graphics;

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

	private Consumer<Graphics> renderer = (g) -> {
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
	 * @param consumer
	 *            the rendering delgate
	 */
	void setRenderingHook(Consumer<Graphics> consumer) {
		this.renderer = consumer;
	}

	public void doPaint() {
		buffer.show();
		java.awt.Graphics g = buffer.getDrawGraphics();
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		this.renderer.accept(new Graphics((Graphics2D) g));
		g.dispose();
	}

}
