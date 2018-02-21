package ch.awae.simtrack.window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.scene.Buffer;
import ch.awae.simtrack.scene.Graphics;
import ch.awae.simtrack.scene.Window;
import lombok.Getter;

public class MainWindow extends JFrame implements Window {
	private static final long serialVersionUID = -6535504689285114239L;

	private final Surface canvas;
	private final @Getter Buffer buffer;
	private final @Getter Input input;

	public MainWindow(int width, int height) {
		super("SimTrack");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.canvas = new Surface(width, height);
		this.add(this.canvas);
		this.pack();
		this.setResizable(false);
		
		this.setLocation((int) (width / 2 - this.getWidth() / 2), 0);
		this.setFocusTraversalKeysEnabled(false);
		this.setVisible(true);
		this.setIgnoreRepaint(true);

		this.canvas.initBuffer();
		this.buffer = new BufferImpl(this.canvas.buffer, width, height);
		this.input = new Input();
		this.canvas.addMouseListener(input.getMouse());
		this.canvas.addMouseMotionListener(input.getMouse());
		this.canvas.addMouseWheelListener(input.getMouse());
		this.addKeyListener(input.getKeyboard());
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

	@Override
	public void setTitle(String title) {
		if (title == null || title.isEmpty())
			super.setTitle("SimTrack");
		else
			super.setTitle("SimTrack - " + title);
	}

	private static class Surface extends Canvas {
		private static final long serialVersionUID = 8065383257596369606L;

		public BufferStrategy buffer;

		Surface(int x, int y) {
			super();
			this.setSize(x, y);
			this.setMinimumSize(new Dimension(x, y));
			this.setPreferredSize(new Dimension(x, y));
			setIgnoreRepaint(true);
			this.setFocusable(false);
		}

		void initBuffer() {
			createBufferStrategy(2);
			buffer = getBufferStrategy();
		}

	}

	@Override
	public int getCanvasWidth() {
		return canvas.getWidth();
	}

	@Override
	public int getCanvasHeight() {
		return canvas.getHeight();
	}

}
