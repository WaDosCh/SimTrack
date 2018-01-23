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

import ch.awae.simtrack.controller.tools.BuildTool;
import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IView;

/**
 * top-level management of the active side of the user interface. It manages the
 * activation of the different editor tools. It thereby targets reduction of
 * complexity by delegating the user actions to well-defined tools.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class Editor {

	private IController owner;

	private ITool currentTool;
	private IRenderer renderer;
	private HashMap<String, ITool> tools = new HashMap<>();

	/**
	 * instantiates a new editor for the given controller.
	 * 
	 * @param c
	 *            the controller
	 */
	Editor(IController c) {
		this.owner = c;
		loadTools();
	}

	/**
	 * adds a new tool to the editor. If this is the first tool added to the
	 * editor, it will be directly activated with a {@code null} argument.
	 * 
	 * @param tool
	 *            the tool to add.
	 */
	public void addTool(ITool tool) {
		this.tools.put(tool.getToolName(), tool);
		if (this.currentTool == null) {
			loadTool(tool.getToolName(), null);
		}
	}

	/**
	 * retrieves the controller instance owning this editor
	 * 
	 * @return the owning controller instance
	 */
	public IController getController() {
		return this.owner;
	}

	/**
	 * loads a tool and unloads the current one. If the new tool cannot be
	 * loaded, the current one will not be unloaded and stays active.
	 * 
	 * @param name
	 *            the identifier string of the new tool
	 * @param args
	 *            additional arguments to hand over to the new tool
	 * @return {@code true} if the tool switch was successful
	 */
	public boolean loadTool(String name, Object[] args) {
		ITool next = this.tools.get(name);
		if (next == null)
			return false;
		if (this.currentTool == next) {
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
		if (this.currentTool != null)
			this.currentTool.unload();
		this.currentTool = next;
		this.renderer = this.currentTool.getRenderer();
		return true;
	}

	/**
	 * initialise all available tools
	 */
	private void loadTools() {
		addTool(new FreeTool(this));
		addTool(new BuildTool(this));
	}

	/**
	 * renders the currently active tool
	 * 
	 * @param g
	 *            the graphics instance to render onto
	 * @param view
	 *            the view
	 */
	void render(Graphics2D g, IView view) {
		if (this.renderer != null)
			this.renderer.render((Graphics2D) g.create(), view);
	}

	/**
	 * performs an editor update tick
	 */
	void tick() {
		if (this.currentTool != null)
			this.currentTool.tick();
	}

}
