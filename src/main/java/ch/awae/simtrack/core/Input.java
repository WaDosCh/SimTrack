package ch.awae.simtrack.core;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;

import ch.judos.generic.data.HashMapList;
import lombok.Getter;

public class Input {

	// CONSTANTS
	public static final int MOUSE_LEFT = 0x0001_0000;
	public static final int MOUSE_CENTER = 0x0002_0000;
	public static final int MOUSE_RIGHT = 0x0003_0000;

	// MOUSE MOTION
	private double scroll = 0;
	private Point mousePosition = new Point(0, 0);

	// LISTENER REGISTRY
	private HashMap<Integer, Binding> keycodeBindings = new HashMap<>();
	private HashMap<Action, Binding> actionBindings = new HashMap<>();
	private HashMapList<Integer, Binding> bindings = new HashMapList<>();

	// BINDING PROVIDERS
	public synchronized Binding getBinding(int keycode) {
		Binding binding = keycodeBindings.get(keycode);
		if (binding == null) {
			binding = new Binding();
			keycodeBindings.put(keycode, binding);
			bindings.put(keycode, binding);
		}
		return binding;
	}

	public Binding getBinding(Action action) {
		Binding binding = actionBindings.get(action);
		if (binding == null) {
			binding = new Binding();
			actionBindings.put(action, binding);
			for (int keycode : action.getKeyCodes()) {
				bindings.put(keycode, binding);
			}
		}
		return binding;
	}

	// MOUSE UTILITIES
	/**
	 * provides the current mouse scroll speed.
	 * 
	 * @return the current scroll speed.
	 */
	public double getScroll() {
		double result = this.scroll;
		this.scroll = 0;
		return result;
	}

	public Point getMousePosition() {
		return new Point(this.mousePosition);
	}

	// EVENT HANLDING
	private void handle(int keycode, boolean state) {
		ArrayList<Binding> list = bindings.getList(keycode);
		if (list != null) {
			for (Binding binding : list) {
				binding.update(state);
			}
		}
	}

	// ADAPTERS
	private @Getter KeyAdapter keyboard = new KeyAdapter() {
		@Override
		public synchronized void keyPressed(KeyEvent e) {
			handle(e.getKeyCode(), true);
		}

		@Override
		public synchronized void keyReleased(KeyEvent e) {
			handle(e.getKeyCode(), false);
		}
	};

	private @Getter MouseAdapter mouse = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			this.mouseMoved(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = e.getPoint();
			if (p == null)
				return;
			Input.this.mousePosition = p;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			this.setBtn(e.getButton(), true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			this.setBtn(e.getButton(), false);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Input.this.scroll = e.getPreciseWheelRotation();
		}

		private void setBtn(int button, boolean value) {
			switch (button) {
				case MouseEvent.BUTTON1:
					handle(MOUSE_LEFT, value);
					break;
				case MouseEvent.BUTTON2:
					handle(MOUSE_CENTER, value);
					break;
				case MouseEvent.BUTTON3:
					handle(MOUSE_RIGHT, value);
					break;
				default:
					break;
			}
		}

	};

}
