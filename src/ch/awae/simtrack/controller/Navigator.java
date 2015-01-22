package ch.awae.simtrack.controller;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.vecmath.Vector2f;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.view.ARenderer;

public class Navigator implements ITool {

	private MouseWheelListener scrollListener = new MouseWheelListener() {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			scroll(e);
		}
	};

	@Override
	public void load() throws IllegalStateException {
		Global.window.getContentPane().addMouseWheelListener(
				this.scrollListener);
		Global.window.addKeyListener(this.a);
	}

	@Override
	public void unload() {
		// no action required
	}

	boolean kUp, kDown, kLeft, kRight;

	private KeyAdapter a = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			char c = e.getKeyChar();
			switch (c) {
			case 'a':
				Navigator.this.kLeft = true;
				break;
			case 'd':
				Navigator.this.kRight = true;
				break;
			case 'w':
				Navigator.this.kUp = true;
				break;
			case 's':
				Navigator.this.kDown = true;
				break;
			default:
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();
			switch (c) {
			case 'a':
				Navigator.this.kLeft = false;
				break;
			case 'd':
				Navigator.this.kRight = false;
				break;
			case 'w':
				Navigator.this.kUp = false;
				break;
			case 's':
				Navigator.this.kDown = false;
				break;
			default:
				break;
			}
		}
	};

	private final static int BORDER = 20;
	private final static int MOVE_SPEED = 10;

	@Override
	public void tick() {
		Point mouse = Global.mouseObserver.mouse;
		if (mouse == null)
			return;
		int dx = 0, dy = 0;
		if (mouse.x < BORDER || this.kLeft)
			dx = 1;
		if (mouse.y < BORDER || this.kUp)
			dy = 1;
		if (mouse.x > Global.ScreenW - BORDER || this.kRight)
			dx = -1;
		if (mouse.y > Global.ScreenH - BORDER || this.kDown)
			dy = -1;
		dx *= MOVE_SPEED;
		dy *= MOVE_SPEED;
		Global.port.moveScene(dx, dy);
	}

	@Override
	public String getToolName() {
		return null;
	}

	@Override
	public ARenderer getRenderer() {
		return null;
	}

	private final static int approachPerStep = 0;
	private final static int deltaZoom = 1;

	@SuppressWarnings("javadoc")
	public void scroll(MouseWheelEvent e) {
		double amount = e.getPreciseWheelRotation();
		Point mouse = Global.mouseObserver.mouse;
		if (mouse == null)
			mouse = new Point(0, 0);
		Global.port.zoom((int) (amount * deltaZoom), mouse.x, mouse.y);

	}

}
