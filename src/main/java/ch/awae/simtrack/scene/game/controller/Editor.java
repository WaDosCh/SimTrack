package ch.awae.simtrack.scene.game.controller;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.input.InputHandler;
import ch.awae.simtrack.scene.game.controller.tools.Tool;
import lombok.NonNull;

/**
 * top-level management of the active side of the user interface. It manages the activation of the different editor
 * tools. It thereby targets reduction of complexity by delegating the user actions to well-defined tools.
 */
public class Editor implements BaseTicker, BaseRenderer, InputHandler {

	private Logger logger = LogManager.getLogger(getClass());

	private Tool currentTool;
	private BaseRenderer renderer;
	private HashMap<Class<? extends Tool>, Tool> tools = new HashMap<>();
	private Class<? extends Tool> baseToolClass = null;

	/**
	 * instantiates a new editor for the given scene.
	 * 
	 * @param scene the scene
	 */
	public Editor() {
	}

	@Override
	public String getName() {
		if (currentTool == null)
			return "Editor";
		else
			return "Editor (" + currentTool.getName() + ")";
	}

	/**
	 * adds a new tool to the editor. If this is the first tool added to the editor, it will be directly activated with
	 * a {@code null} argument.
	 * 
	 * @param tool the tool to add.
	 */
	public void addTool(@NonNull Tool tool) {
		this.tools.put(tool.getClass(), tool);
		if (this.currentTool == null) {
			loadTool(tool.getClass());
			baseToolClass = tool.getClass();
		}
	}

	/**
	 * loads a tool and unloads the current one. If the new tool cannot be loaded, the current one will not be unloaded
	 * and stays active.
	 * 
	 * @param name the identifier string of the new tool
	 * @param args additional arguments to hand over to the new tool
	 * @return {@code true} if the tool switch was successful
	 */
	public boolean loadTool(Class<? extends Tool> toolClazz, Object... args) {
		if (toolClazz == null)
			throw new RuntimeException("May not load null tool");
		logger.debug("Load tool: " + toolClazz.getSimpleName() + "[" + StringUtils.join(args, ",") + "]");
		Tool next = this.tools.get(toolClazz);
		if (next == null) {
			throw new RuntimeException("Tool " + toolClazz.getSimpleName() + " was not registered");
		}

		if (this.currentTool != next && this.currentTool != null)
			this.currentTool.unloadTool();

		next.loadTool(args);
		this.currentTool = next;
		this.renderer = this.currentTool.getRenderer();

		return true;
	}

	/**
	 * renders the currently active tool
	 * 
	 * @param g the graphics instance to render onto
	 */
	@Override
	public void render(Graphics g) {
		if (this.renderer != null) {
			GraphicsStack stack = g.getStack();
			this.renderer.render(g);
			g.setStack(stack);
		}
	}

	@Override
	public void tick() {
		if (this.currentTool != null)
			this.currentTool.tick();
	}

	@Override
	public void handleInput(InputEvent event) {
		if (this.currentTool != null)
			this.currentTool.handleInput(event);
	}

}
