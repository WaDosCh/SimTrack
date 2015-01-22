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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.Timer;

import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.renderer.BaseTrackRenderer;
import ch.awae.simtrack.view.renderer.BorderConnectionRenderer;
import ch.awae.simtrack.view.renderer.EditorRenderer;
import ch.awae.simtrack.view.renderer.HexGridRenderer;

/**
 * Core Rendering Control
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class RenderingController {

	private static Timer timer;
	private static ArrayList<ARenderer> rends = new ArrayList<>();

	private static int delC = 0;
	private static int tAvg = 0;

	public static void init(int fps) {
		timer = new Timer(1000 / fps, e -> Window.INSTANCE.repaint());
		timer.setRepeats(true);
	}

	static {
		rends.add(new BorderConnectionRenderer());
		rends.add(new BaseTrackRenderer());
		rends.add(new HexGridRenderer());
		rends.add(new EditorRenderer());
	}

	public static void render(Graphics g) {
		long a = System.currentTimeMillis();
		if (rends == null)
			return;
		for (ARenderer r : rends)
			r.render((Graphics2D) g.create());

		// RENDERING ANALYTICS
		tAvg += System.currentTimeMillis() - a;
		if (++delC == 49) {
			Window.instance().setTitle(
					"Rendering Pass Time: " + (tAvg * 1000 / delC) + " ns");
			tAvg = delC = 0;
		}

	}

	public static void start() {
		timer.start();
	}

	public static void stop() {
		timer.stop();
	}

}
