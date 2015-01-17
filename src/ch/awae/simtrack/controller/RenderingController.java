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
package ch.awae.simtrack.controller;

import javax.swing.Timer;

import ch.awae.simtrack.gui.Window;

public class RenderingController {

	private Window w;
	private Timer t;

	public RenderingController(Window w, int fps) {
		assert w != null;
		this.w = w;
		this.t = new Timer(1000 / fps, e -> this.w.repaint());
		this.t.setRepeats(true);
	}

	public void start() {
		this.t.start();
	}

	public void stop() {
		this.t.stop();
	}

}
