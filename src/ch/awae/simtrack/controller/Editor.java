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
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.view.ARenderer;

/**
 * top-level management of the active side of the user interface. It manages the
 * activation of the different editor tools. It thereby targets reduction of
 * complexity by delegating the user actions to well-defined tools.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-17
 * @since SimTrack 0.0.1
 */
public class Editor {

	private HashMap<String, ITool> tools = new HashMap<>();
	private ARenderer renderer;
	private ITool currentTool;
	private Timer t;
	private Navigator nav = new Navigator();
	private TrackBar bar = new TrackBar();
	private ARenderer barRend;

	public Editor(int logicTicks) {
		this.loadTools();
		this.t = new Timer(1000 / logicTicks, e -> this.tick());
		this.t.setRepeats(true);
		this.barRend = this.bar.getRenderer();
		this.nav.load();
		this.bar.load();
	}

	public void tick() {
		this.nav.tick();
		this.bar.tick();
		if (this.currentTool != null)
			this.currentTool.tick();
	}

	public void render(Graphics2D g) {
		if (this.renderer != null)
			this.renderer.render((Graphics2D) g.create());
		this.barRend.render(g);
	}

	public void addTool(ITool tool) {
		this.tools.put(tool.getToolName(), tool);
		if (this.currentTool == null) {
			this.loadTool(tool.getToolName());
		}
	}

	public boolean loadTool(String name) {
		ITool next = tools.get(name);
		if (next == currentTool || next == null)
			return false;
		try {
			next.load();
		} catch (IllegalStateException ex) {
			// could not load. do not make the switch and leave old tool on!
			return false;
		}
		if (this.currentTool != null)
			this.currentTool.unload();
		this.currentTool = next;
		this.renderer = this.currentTool.getRenderer();
		return true;
	}

	private void loadTools() {
		this.addTool(new FreeTool());
	}

	public void start() {
		this.t.start();
	}

	public void stop() {
		this.t.stop();
	}

}
