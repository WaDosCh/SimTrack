package ch.awae.simtrack.core.input;

import static ch.awae.simtrack.core.input.InputEvent.InputEventType.CHANGE;
import static ch.awae.simtrack.core.input.InputEvent.InputEventType.PRESS;
import static ch.awae.simtrack.core.input.InputEvent.InputEventType.RELEASE;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.NamedComponent;
import lombok.Getter;

public class InputController implements NamedComponent {

	protected final Logger logger = LogManager.getLogger();

	// CONSTANTS
	public static final int MOUSE_LEFT = 0x0001_0000;
	public static final int MOUSE_CENTER = 0x0002_0000;
	public static final int MOUSE_RIGHT = 0x0003_0000;
	public static final int MOUSE_SCROLL = 0x0004_0000;

	// EVENT PROCESSING
	private List<InputEvent> queuedInputEvents;

	// CACHE
	private HashSet<Integer> holdKeyCodes;
	private Point currentMousePosition;
	private @Getter InputHandler currentlyFocused;

	public InputController() {
		queuedInputEvents = new ArrayList<>();
		this.holdKeyCodes = new HashSet<>();
		this.currentMousePosition = new Point(-1, -1);
	}

	public List<InputEvent> popAllEvents() {
		List<InputEvent> result = queuedInputEvents;
		queuedInputEvents = new ArrayList<>();
		return result;
	}

	// ADAPTERS
	private @Getter KeyAdapter keyboard = new KeyAdapter() {
		@Override
		public synchronized void keyPressed(KeyEvent e) {
			queuedInputEvents.add(new InputEvent(e.getKeyCode(), PRESS, 0, holdKeyCodes, currentMousePosition));
			holdKeyCodes.add(e.getKeyCode());
		}

		@Override
		public synchronized void keyReleased(KeyEvent e) {
			holdKeyCodes.remove(e.getKeyCode());
			queuedInputEvents.add(new InputEvent(e.getKeyCode(), RELEASE, 0, holdKeyCodes, currentMousePosition));
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
			currentMousePosition = p;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int code = btnToCode(e.getButton());
			queuedInputEvents.add(new InputEvent(code, PRESS, 0, holdKeyCodes, currentMousePosition));
			holdKeyCodes.add(code);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int code = btnToCode(e.getButton());
			holdKeyCodes.remove(code);
			queuedInputEvents.add(new InputEvent(code, RELEASE, 0, holdKeyCodes, currentMousePosition));
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int code = MOUSE_SCROLL;
			queuedInputEvents
					.add(new InputEvent(code, CHANGE, e.getPreciseWheelRotation(), holdKeyCodes, currentMousePosition));
		}

		private int btnToCode(int button) {
			if (button == MouseEvent.BUTTON1)
				return MOUSE_LEFT;
			if (button == MouseEvent.BUTTON2)
				return MOUSE_CENTER;
			if (button == MouseEvent.BUTTON3)
				return MOUSE_RIGHT;
			logger.warn("Unknown mouse button: " + button);
			return MOUSE_CENTER;
		}

	};

	public Point getMousePosition() {
		return this.currentMousePosition;
	}

	public void setFocus(InputHandler handler) {
		this.currentlyFocused = handler;
	}

	public void unfocus(InputHandler handler) {
		if (this.currentlyFocused == handler)
			this.currentlyFocused = null;
	}

	public void handleInput(InputEvent event) {
		if (this.currentlyFocused != null)
			this.currentlyFocused.handleInput(event);
	}

}
