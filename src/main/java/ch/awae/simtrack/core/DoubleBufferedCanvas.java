package ch.awae.simtrack.core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class DoubleBufferedCanvas extends Canvas {
	private static final long serialVersionUID = 8065383257596369606L;

	private BufferStrategy buffer;

	public DoubleBufferedCanvas(int x, int y) {
		this.setSize(x, y);
		this.setMinimumSize(new Dimension(x, y));
		this.setPreferredSize(new Dimension(x, y));
		this.setMaximumSize(new Dimension(x, y));

		setIgnoreRepaint(true);
		this.setFocusable(false);
	}

	public Buffer initBuffer() {
		createBufferStrategy(2);
		buffer = getBufferStrategy();
		return new BufferImpl(buffer, getWidth(), getHeight());
	}

	public void disposeBuffer() {
		getBufferStrategy().dispose();
	}

	private static class BufferImpl implements Buffer {

		private final BufferStrategy buffer;
		private Graphics graphics = null;
		private int width, height;

		public BufferImpl(BufferStrategy buffer, int width, int height) {
			this.buffer = buffer;
			this.width = width;
			this.height = height;
		}

		public void swapBuffer() {
			if (graphics != null)
				graphics.dispose();
			buffer.show();
			graphics = new Graphics((Graphics2D) buffer.getDrawGraphics());
		}

		public void clearBuffer() {
			graphics.clearRect(0, 0, width, height);
		}

		public Graphics getGraphics() {
			return graphics;
		}

	}

}