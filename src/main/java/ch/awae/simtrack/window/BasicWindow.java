package ch.awae.simtrack.window;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Input;
import ch.awae.simtrack.core.RootWindow;

public class BasicWindow implements RootWindow {

	JFrame f;
	JPanel p;
	Image i;
	Graphics g;
	private Input input;

	private final List<Consumer<Image>> snapshotRequests = new ArrayList<>();

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
		if (!snapshotRequests.isEmpty())
			takeSnapshot();
		g.clearRect(0, 0, p.getWidth(), p.getHeight());
	}

	private void takeSnapshot() {
		BufferedImage snap = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_RGB);
		snap.getGraphics().drawImage(i, 0, 0, null);
		synchronized (snapshotRequests) {
			for (Consumer<Image> consumer : snapshotRequests) {
				consumer.accept(snap);
			}
		}
	}

	@Override
	public Graphics getGraphics() {
		return g;
	}

	@Override
	public void init(Input input) {
		f.setVisible(true);
		i = p.createImage(p.getWidth(), p.getHeight());
		g = new Graphics((Graphics2D) i.getGraphics());
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

	@Override
	public void takeSnapshot(Consumer<Image> callback) {
		synchronized (snapshotRequests) {
			snapshotRequests.add(callback);
		}
	}

}
