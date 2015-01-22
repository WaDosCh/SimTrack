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

import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.Timer;

import ch.awae.simtrack.controller.tools.BuildTool;
import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.view.IRenderer;

/**
 * top-level management of the active side of the user interface. It manages the
 * activation of the different editor tools. It thereby targets reduction of
 * complexity by delegating the user actions to well-defined tools.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Editor {

	private static TrackBar bar = new TrackBar();
	private static IRenderer barRend;
	private static ITool currentTool;
	private static IRenderer renderer;
	private static Timer t;
	private static HashMap<String, ITool> tools = new HashMap<>();

	public static void addTool(ITool tool) {
		tools.put(tool.getToolName(), tool);
		if (currentTool == null) {
			loadTool(tool.getToolName(), null);
		}
	}

	public static void init(int logicTicks) {
		loadTools();
		t = new Timer(1000 / logicTicks, e -> Editor.tick());
		t.setRepeats(true);
		barRend = bar.getRenderer();
	}

	public static boolean loadTool(String name, Object[] args) {
		ITool next = tools.get(name);
		if (next == null)
			return false;
		if (currentTool == next) {
			next.unload();
			next.load(args);
			return true;
		}
		try {
			next.load(args);
		} catch (IllegalStateException ex) {
			// could not load. do not make the switch and leave old tool on!
			return false;
		}
		if (currentTool != null)
			currentTool.unload();
		currentTool = next;
		renderer = currentTool.getRenderer();
		return true;
	}

	private static void loadTools() {
		addTool(new FreeTool());
		addTool(new BuildTool());
	}

	public static void render(Graphics2D g) {
		if (renderer != null)
			renderer.render((Graphics2D) g.create());
		barRend.render(g);
	}

	public static void start() {
		t.start();
	}

	public static void stop() {
		t.stop();
	}

	private static void tick() {
		Navigator.tick();
		bar.tick();
		if (currentTool != null)
			currentTool.tick();
	}

}
