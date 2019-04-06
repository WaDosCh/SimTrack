package ch.awae.simtrack.window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputController;
import lombok.Getter;
import ch.awae.simtrack.core.GameWindow;

public class NativeFullscreen implements GameWindow {

	private @Getter InputController input;
	private @Getter Graphics graphics;
	private @Getter Dimension screenSize;

	private GraphicsDevice screen;
	private JFrame window;
	private Canvas canvas;
	private BufferStrategy buffer;

	public NativeFullscreen(InputController input) {
		this.input = input;
		screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		window = new JFrame();
		canvas = new Canvas();
		Rectangle bounds = screen.getDefaultConfiguration().getBounds();
		this.screenSize = bounds.getSize();
		window.add(canvas);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
	}

	private void init() {

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
	public void flipFrame() {
		if (graphics != null)
			graphics.dispose();
		buffer.show();
		graphics = new Graphics((Graphics2D) buffer.getDrawGraphics());
		graphics.clearRect(0, 0, this.screenSize.width, this.screenSize.height);
	}

}
