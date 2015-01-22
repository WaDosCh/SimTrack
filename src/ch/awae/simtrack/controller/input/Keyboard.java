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
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Keyboard {

	private static KeyAdapter adapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			Keyboard.keystates.put(e.getKeyCode(), Boolean.TRUE);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Keyboard.keystates.put(e.getKeyCode(), Boolean.FALSE);
		}
	};

	static HashMap<Integer, Boolean> keystates = new HashMap<>();

	public static void init() {
		Window.INSTANCE.addKeyListener(Keyboard.adapter);
	}

	public static boolean key(int keyCode) {
		return keystates.getOrDefault(keyCode, Boolean.FALSE).booleanValue();
	}

	public static boolean keysAnd(int... keyCode) {
		for (int code : keyCode)
			if (!key(code))
				return false;
		return true;
	}

	public static boolean keysOr(int... keyCode) {
		for (int code : keyCode)
			if (key(code))
				return true;
		return false;
	}

	public static void reset() {
		keystates.clear();
	}

}
