/*
w * SimTrack - Railway Planning and Simulation Game
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

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.tools.BuildTool;
import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.controller.tools.InGameMenu;
import ch.awae.simtrack.controller.tools.PathFindingTool;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;

/**
 * top-level management of the active side of the user interface. It manages the
 * activation of the different editor tools. It thereby targets reduction of
 * complexity by delegating the user actions to well-defined tools.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class Editor implements IEditor {

	private IController owner;

	private ITool currentTool;
	private IRenderer renderer;
	private HashMap<Class<? extends ITool>, ITool> tools = new HashMap<>();

	private Keyboard keyboard;
	private Input input;
	private Mouse mouse;

	private IModel model;

	private InGameMenu ingameMenu;

	/**
	 * instantiates a new editor for the given controller.
	 * 
	 * @param c
	 *            the controller
	 */
	Editor(IController c) {
		this.owner = c;
		this.mouse = c.getMouse();
		this.keyboard = c.getKeyboard();
		this.input = c.getInput();
		this.model = c.getModel();
		loadTools();
	}

	public Input getInput() {
		return owner.getInput();
	}

	/**
	 * adds a new tool to the editor. If this is the first tool added to the
	 * editor, it will be directly activated with a {@code null} argument.
	 * 
	 * @param tool
	 *            the tool to add.
	 */
	private void addTool(ITool tool) {
		this.tools.put(tool.getClass(), tool);
		if (this.currentTool == null) {
			loadTool(tool.getClass());
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

	@Override
	public boolean loadTool(Class<? extends ITool> toolClass, Object... args) {
		if (toolClass == null)
			toolClass = FreeTool.class;
		Log.info("Load tool: ", toolClass.getSimpleName());
		ITool next = this.tools.get(toolClass);
		if (next == null) {
			Log.warn("Tool " + toolClass.getSimpleName() + " was not found.");
			return false;
		}
		if (this.currentTool == next) {
			next.onUnload();
			next.load(args);
			return true;
		}
		try {
			next.load(args);
		}
		catch (IllegalStateException ex) {
			Log.err("Error loading tool", ex.getMessage());
			// could not load. do not make the switch and leave old tool on!
			return false;
		}
		if (this.currentTool != null)
			this.currentTool.onUnload();
		this.currentTool = next;
		this.renderer = this.currentTool.getRenderer();
		this.getController().setWindowTitle(toolClass.getSimpleName());
		return true;
	}

	/**
	 * initialise all available tools
	 */
	private void loadTools() {
		addTool(new FreeTool(this));
		addTool(new BuildTool(this));
		addTool(new PathFindingTool(this));
		this.ingameMenu = new InGameMenu(this.mouse, this.keyboard, (IEditor) this, this.model,
			this.owner);
		addTool(this.ingameMenu);
	}

	/**
	 * renders the currently active tool
	 * 
	 * @param g
	 *            the graphics instance to render onto
	 * @param view
	 *            the view
	 */
	void render(Graphics2D g, IGameView view) {
		if (this.renderer != null)
			this.renderer.renderSafe(g, view);
	}

	/**
	 * performs an editor update tick
	 */
	void tick() {
		if (this.currentTool != null)
			this.currentTool.tick();
	}

	public void setModel(IModel model) {
		this.model = model;
		this.ingameMenu.setModel(this.model);
	}

}
