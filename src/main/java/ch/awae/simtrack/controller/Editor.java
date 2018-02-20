package ch.awae.simtrack.controller;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.controller.tools.BuildTool;
import ch.awae.simtrack.controller.tools.FreeTool;
import ch.awae.simtrack.controller.tools.InGameMenu;
import ch.awae.simtrack.controller.tools.PathFindingTool;
import ch.awae.simtrack.controller.tools.SignalTool;
import ch.awae.simtrack.util.ReflectionHelper;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.renderer.IRenderer;

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

	private Logger logger = LogManager.getLogger(getClass());

	private ITool currentTool;
	private IRenderer renderer;
	private HashMap<Class<? extends ITool>, ITool> tools = new HashMap<>();

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

	private void unloadCurrentTool() {
		if (currentTool != null) {
			ReflectionHelper<ITool> oldHelper = new ReflectionHelper<ITool>(currentTool);
			try {
				oldHelper.findAndInvokeCompatibleMethod(OnUnload.class, null, new Object[] {});
			} catch (NoSuchMethodException nsm) {
				// ignore
			} catch (Exception e) {
				logger.error("failure to unload", e);
			}
		}
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
	public boolean loadTool(Class<? extends ITool> toolClass, Object... args) {
		if (toolClass == null)
			toolClass = FreeTool.class;
		logger.info("Load tool: " + toolClass.getSimpleName() + "[" + StringUtils.join(args, ",") + "]");
		ITool next = this.tools.get(toolClass);

		if (next == null) {
			logger.warn("Tool " + toolClass.getSimpleName() + " was not found.");
			return false;
		}

		try {
			ReflectionHelper<ITool> helper = new ReflectionHelper<>(next);

			try {
				helper.findAndInvokeCompatibleMethod(OnLoad.class, null, args);
			} catch (NoSuchMethodException nsm) {
				// ignore
				if (args.length > 0)
					throw new IllegalArgumentException("no method found for non-empty parameter list");
			}
			if (currentTool != next)
				unloadCurrentTool();

			this.currentTool = next;
			this.renderer = this.currentTool.getRenderer();
			this.getController().setWindowTitle(toolClass.getSimpleName());

			return true;

		} catch (Exception exception) {
			logger.error("Error loading tool: " + exception.getMessage());
			// error during tool load. remain with old tool
			return false;
		}
	}

	/**
	 * initialise all available tools
	 */
	private void loadTools() {
		addTool(new FreeTool(this));
		addTool(new BuildTool(this));
		addTool(new PathFindingTool(this));
		addTool(new InGameMenu(this));
		addTool(new SignalTool(this));
	}

	/**
	 * renders the currently active tool
	 * 
	 * @param g
	 *            the graphics instance to render onto
	 * @param view
	 *            the view
	 */
	void render(Graphics g, IGameView view) {
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

}
