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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Timer;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.renderer.BaseTrackRenderer;
import ch.awae.simtrack.view.renderer.BorderConnectionRenderer;
import ch.awae.simtrack.view.renderer.EditorRenderer;
import ch.awae.simtrack.view.renderer.HexGridRenderer;

public class RenderingController {

	private Window w;
	private Timer t;
	private ArrayList<ARenderer> rends;

	private int delC = 0;
	private int tAvg = 0;

	public RenderingController(Window w, int fps) {
		assert w != null;
		this.w = w;
		this.rends = new ArrayList<>();
		this.init();
		this.t = new Timer(1000 / fps, e -> this.w.repaint());
		this.t.setRepeats(true);
	}

	private void init() {
		this.rends.add(new BorderConnectionRenderer());
		this.rends.add(new BaseTrackRenderer());
		this.rends.add(new HexGridRenderer());
		this.rends.add(new EditorRenderer());
	}

	public void render(Graphics g) {
		long a = System.currentTimeMillis();
		if (this.rends == null)
			return;
		for (ARenderer r : this.rends)
			r.render((Graphics2D) g.create());

		this.tAvg += System.currentTimeMillis() - a;
		if (++this.delC == 49) {
			// System.out.println("average rendering time: "
			// + (this.tAvg * 1.0 / this.delC));
			Global.window.setTitle("Rendering Pass Time: "
					+ (this.tAvg * 1000 / this.delC) + " ns");
			this.tAvg = this.delC = 0;
		}

	}

	public void start() {
		this.t.start();
	}

	public void stop() {
		this.t.stop();
	}

}
