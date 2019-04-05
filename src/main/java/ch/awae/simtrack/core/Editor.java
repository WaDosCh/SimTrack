package ch.awae.simtrack.core;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.scene.game.Game;
import lombok.NonNull;

/**
 * top-level management of the active side of the user interface. It manages the
 * activation of the different editor tools. It thereby targets reduction of
 * complexity by delegating the user actions to well-defined tools.
 * 
 * @author Andreas Wälchli
 * @version 2.2, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class Editor implements BaseTicker, BaseRenderer {

	private Game scene;

	private Logger logger = LogManager.getLogger(getClass());

	private Tool currentTool;
	private BaseRenderer renderer;
	private HashMap<Class<? extends Tool>, Tool> tools = new HashMap<>();
	private Class<? extends Tool> baseToolClass = null;

	/**
	 * instantiates a new editor for the given scene.
	 * 
	 * @param scene
	 *            the scene
	 */
	public Editor(@NonNull Game scene) {
		this.scene = scene;
	}

	public Input getInput() {
		return scene.getInput();
	}

	@Override
	public String getName() {
		if (currentTool == null)
			return "Editor";
		else
			return "Editor (" + currentTool.getName() + ")";
	}

	/**
	 * adds a new tool to the editor. If this is the first tool added to the
	 * editor, it will be directly activated with a {@code null} argument.
	 * 
	 * @param tool
	 *            the tool to add.
	 */
	public void addTool(@NonNull Tool tool) {
		this.tools.put(tool.getClass(), tool);
		if (this.currentTool == null) {
			loadTool(tool.getClass());
			baseToolClass = tool.getClass();
		}
	}

	/**
	 * retrieves the controller instance owning this editor
	 * 
	 * @return the owning controller instance
	 */
	public Game getScene() {
		return this.scene;
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
	public boolean loadTool(Class<? extends Tool> toolClazz, Object... args) {
		@NonNull
		Class<? extends Tool> toolClass = (toolClazz == null) ? baseToolClass : toolClazz;
		logger.debug("Load tool: " + toolClass.getSimpleName() + "[" + StringUtils.join(args, ",") + "]");
		Tool next = this.tools.get(toolClass);

		if (next == null) {
			logger.warn("Tool " + toolClass.getSimpleName() + " was not found.");
			return false;
		}

		try {
			next.loadTool(args);
			if (this.currentTool != next && this.currentTool != null)
				this.currentTool.unloadTool();

			this.currentTool = next;
			this.renderer = this.currentTool.getRenderer();

			return true;

		} catch (Exception exception) {
			logger.error("Error loading tool: " + exception.getMessage());
			exception.printStackTrace();
			// error during tool load. remain with old tool
			return false;
		}
	}

	/**
	 * renders the currently active tool
	 * 
	 * @param g
	 *            the graphics instance to render onto
	 * @param view
	 *            the view
	 */
	@Override
	public void render(Graphics g) {
		if (this.renderer != null)
			try {
				GraphicsStack stack = g.getStack();
				this.renderer.render(g);
				g.setStack(stack);
			} catch (Exception e) {
				logger.error("failed to render " + currentTool.getClass().getSimpleName(), e);
			}
	}

	@Override
	public void tick() {
		if (this.currentTool != null)
			this.currentTool.tick();
	}

}
