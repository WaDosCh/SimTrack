package ch.awae.simtrack.window;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.GameWindow;

public class NativeFullscreen implements GameWindow {

	private GraphicsDevice screen;
	private JFrame window;
	private Canvas canvas;
	private Input input;
	private BufferStrategy buffer;
	private Graphics graphics;
	private int x, y;

	public NativeFullscreen() {
		screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		window = new JFrame();
		canvas = new Canvas();
		Rectangle bounds = screen.getDefaultConfiguration().getBounds();
		x = bounds.width;
		y = bounds.height;
		window.add(canvas);
		System.out.println(screen.isFullScreenSupported());
	}

	@Override
	public int getCanvasWidth() {
		return x;
	}

	@Override
	public int getCanvasHeight() {
		return y;
	}

	@Override
	public void setTitle(String title) {
		// title not supported in fullscreen mode
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public void init(Input input) {
		this.input = input;

		window.setVisible(true);
		window.setResizable(false);

		screen.setFullScreenWindow(window);

		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		window.setVisible(false);
		window.setVisible(true);
		canvas.setFocusable(false);

		canvas.addMouseListener(input.getMouse());
		canvas.addMouseMotionListener(input.getMouse());
		canvas.addMouseWheelListener(input.getMouse());
		window.addKeyListener(input.getKeyboard());

	}

	@Override
	public void discard() {
		buffer.dispose();
		screen.setFullScreenWindow(null);
		
		window.setVisible(false);

		canvas.removeMouseListener(input.getMouse());
		canvas.removeMouseMotionListener(input.getMouse());
		canvas.removeMouseWheelListener(input.getMouse());
		window.removeKeyListener(input.getKeyboard());
	}

	@Override
	public Graphics getGraphics() {
		return this.graphics;
	}

	@Override
	public void flipFrame() {
		if (graphics != null)
			graphics.dispose();
		buffer.show();
		graphics = new Graphics((Graphics2D) buffer.getDrawGraphics());
		graphics.clearRect(0, 0, x, y);
	}

}
