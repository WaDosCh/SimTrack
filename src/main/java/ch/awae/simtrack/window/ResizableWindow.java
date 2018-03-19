package ch.awae.simtrack.window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.GameWindow;

public class ResizableWindow implements GameWindow {

	private JFrame window;
	private Canvas canvas;
	private Input input;
	private BufferStrategy buffer;
	private Graphics graphics;
	private boolean resized = false;
	
	private ComponentListener compListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			resized = true;
		}
	};

	public ResizableWindow(int x, int y) {
		window = new JFrame("SimTrack");
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(x, y));
		window.add(canvas);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public Dimension getCanvasSize() {
		return canvas.getSize();
	}

	@Override
	public void setTitle(String title) {
		if (title == null || title.isEmpty())
			window.setTitle("SimTrack");
		else
			window.setTitle("SimTrack - " + title);
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public void init(Input input) {
		this.input = input;

		window.setIgnoreRepaint(true);
		canvas.setIgnoreRepaint(true);
		window.pack();
		window.setVisible(true);

		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		canvas.setFocusable(false);

		canvas.addMouseListener(input.getMouse());
		canvas.addMouseMotionListener(input.getMouse());
		canvas.addMouseWheelListener(input.getMouse());
		window.addKeyListener(input.getKeyboard());
		window.addComponentListener(compListener);

	}

	@Override
	public void discard() {
		buffer.dispose();

		canvas.removeMouseListener(input.getMouse());
		canvas.removeMouseMotionListener(input.getMouse());
		canvas.removeMouseWheelListener(input.getMouse());
		window.removeKeyListener(input.getKeyboard());
		window.removeComponentListener(compListener);
		
		window.setVisible(false);
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
		graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public boolean resized() {
		boolean res = resized;
		resized = false;
		return res;
	}

}
