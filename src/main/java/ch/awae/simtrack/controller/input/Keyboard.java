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

import ch.awae.simtrack.controller.IGUIControllerHookup;

/**
 * Keyboard Observer. Records any keyboard actions and provides the information
 * in a lightweight way.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class Keyboard {

	private KeyAdapter adapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			Keyboard.this.keystates.put(e.getKeyCode(), Boolean.TRUE);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Keyboard.this.keystates.put(e.getKeyCode(), Boolean.FALSE);
		}
	};

	/**
	 * the recording map for all key states
	 */
	HashMap<Integer, Boolean> keystates = new HashMap<>();

	/**
	 * instantiates a new keyboard observer
	 * 
	 * @param hooker
	 *            the hook-up that should be used by the keyboard. The hook-up
	 *            provides an anonymous way for the keyboard instance to hook
	 *            into the GUI structure without directly accessing it.
	 */
	public Keyboard(IGUIControllerHookup hooker) {
		hooker.getKeyboardHookup().accept(this.adapter);
	}

	/**
	 * checks whether or not a given key is currently pressed.
	 * 
	 * @param keyCode
	 *            the key code of the key to check for. It is recommended to use
	 *            the key-code constants provided by {@link KeyEvent}.
	 * @return {@code true} if and only if the key with the given code is
	 *         currently pressed.
	 */
	public boolean key(int keyCode) {
		return this.keystates.getOrDefault(keyCode, Boolean.FALSE)
				.booleanValue();
	}

	/**
	 * checks whether or not all of the given keys are currently pressed. This
	 * can be useful for checking for key combinations
	 * 
	 * @param keyCode
	 *            an array of all keys that should be checked.
	 * @return {@code true} if and only if all key in the provided list are
	 *         pressed.
	 */
	public boolean keysAnd(int... keyCode) {
		for (int code : keyCode)
			if (!key(code))
				return false;
		return true;
	}

	/**
	 * checks whether or not any of the given keys is currently pressed. This
	 * can be useful for checking multiple key mappings at the same time.
	 * 
	 * @param keyCode
	 *            an array of all the keys that should be checked.
	 * @return {@code true} if any of the given keys is pressed.
	 */
	public boolean keysOr(int... keyCode) {
		for (int code : keyCode)
			if (key(code))
				return true;
		return false;
	}

	/**
	 * resets the internal recordings and thereby virtually neutralises any
	 * currently pressed keys.
	 */
	public void reset() {
		this.keystates.clear();
	}

}