package ch.awae.simtrack.window;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.RootWindow;

public class BasicWindow implements RootWindow {

	JFrame f;
	JPanel p;
	VolatileImage i;
	Graphics g;
	private Input input;

	public BasicWindow(int x, int y) {
		f = new JFrame("SimTrack");
		p = new JPanel();

		p.setPreferredSize(new Dimension(x, y));
		p.setIgnoreRepaint(true);

		f.add(p);
		f.pack();
		f.setIgnoreRepaint(true);
		f.setFocusTraversalKeysEnabled(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
	}

	@Override
	public int getCanvasWidth() {
		return p.getWidth();
	}

	@Override
	public int getCanvasHeight() {
		return p.getHeight();
	}

	@Override
	public void setTitle(String title) {
		if (title == null || title.isEmpty())
			f.setTitle("SimTrack");
		else
			f.setTitle("SimTrack - " + title);
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public void flipFrame() {
		p.getGraphics().drawImage(i, 0, 0, p);
		Toolkit.getDefaultToolkit().sync();
		GraphicsConfiguration gc = p.getGraphicsConfiguration();

		// validate the buffer
		int valCode = i.validate(gc);
		if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
			createBuffer();
		}
		// start rendering
		g.clearRect(0, 0, p.getWidth(), p.getHeight());
	}

	@Override
	public Graphics getGraphics() {
		return g;
	}

	private void createBuffer() {
		GraphicsConfiguration gc = p.getGraphicsConfiguration();
		i = gc.createCompatibleVolatileImage(p.getWidth(), p.getHeight());
		g = new Graphics((Graphics2D) i.getGraphics());
	}

	@Override
	public void init(Input input) {
		f.setVisible(true);
		createBuffer();
		this.input = input;
		p.addMouseListener(input.getMouse());
		p.addMouseMotionListener(input.getMouse());
		p.addMouseWheelListener(input.getMouse());
		f.addKeyListener(input.getKeyboard());
	}

	@Override
	public void discard() {
		i.flush();
		i = null;
		g.dispose();
		g = null;
	}

}
