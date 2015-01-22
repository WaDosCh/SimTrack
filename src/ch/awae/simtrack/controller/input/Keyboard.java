package ch.awae.simtrack.controller.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.gui.Window;

public class Keyboard {

	HashMap<Integer, Boolean> keystates = new HashMap<>();

	private KeyAdapter a = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			Keyboard.this.keystates.put(e.getKeyCode(), Boolean.TRUE);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Keyboard.this.keystates.put(e.getKeyCode(), Boolean.FALSE);
		}
	};

	public Keyboard(Window w) {
		w.addKeyListener(this.a);
	}

	public void reset() {
		this.keystates.clear();
	}

	public boolean key(int keyCode) {
		return this.keystates.getOrDefault(keyCode, Boolean.FALSE)
				.booleanValue();
	}

	public boolean keysOr(int... keyCode) {
		for (int code : keyCode)
			if (this.key(code))
				return true;
		return false;
	}

	public boolean keysAnd(int... keyCode) {
		for (int code : keyCode)
			if (!this.key(code))
				return false;
		return true;
	}

}
