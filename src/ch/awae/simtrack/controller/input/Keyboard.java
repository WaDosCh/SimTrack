/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.awae.simtrack.controller.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import ch.awae.simtrack.gui.Window;

/**
 * Keyboard Observer
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
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
