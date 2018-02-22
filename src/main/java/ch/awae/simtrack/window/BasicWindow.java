package ch.awae.simtrack.window;

import javax.swing.JFrame;

import ch.awae.simtrack.core.Buffer;
import ch.awae.simtrack.core.DoubleBufferedCanvas;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.RootWindow;
import lombok.Getter;

public class BasicWindow extends JFrame implements RootWindow {
	private static final long serialVersionUID = -6535504689285114239L;

	private final DoubleBufferedCanvas canvas;
	private @Getter Buffer buffer;
	private @Getter Input input;

	public BasicWindow(int width, int height) {
		super("SimTrack");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.canvas = new DoubleBufferedCanvas(width, height);
		this.add(this.canvas);
		this.pack();
		this.setResizable(false);

		this.setLocation((int) (width / 2 - this.getWidth() / 2), 0);
		this.setFocusTraversalKeysEnabled(false);
		this.setIgnoreRepaint(true);
	}

	@Override
	public void setTitle(String title) {
		if (title == null || title.isEmpty())
			super.setTitle("SimTrack");
		else
			super.setTitle("SimTrack - " + title);
	}

	@Override
	public int getCanvasWidth() {
		return canvas.getWidth();
	}

	@Override
	public int getCanvasHeight() {
		return canvas.getHeight();
	}

	@Override
	public void init(Input input) {
		this.input = input;
		this.setVisible(true);
		this.buffer = this.canvas.initBuffer();
		this.canvas.addMouseListener(input.getMouse());
		this.canvas.addMouseMotionListener(input.getMouse());
		this.canvas.addMouseWheelListener(input.getMouse());
		this.addKeyListener(input.getKeyboard());
	}

	@Override
	public void discard() {
		this.setVisible(false);
		this.canvas.disposeBuffer();
		this.canvas.removeMouseListener(input.getMouse());
		this.canvas.removeMouseMotionListener(input.getMouse());
		this.canvas.removeMouseWheelListener(input.getMouse());
		this.removeKeyListener(input.getKeyboard());
	}

}
