package ch.awae.simtrack.core;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.Graphics.GraphicsStack;
import ch.awae.simtrack.util.ReflectionHelper;
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
public class Editor<T extends Scene<T>> implements BaseTicker<T>, BaseRenderer<T> {

	private T scene;

	private Logger logger = LogManager.getLogger(getClass());

	private Tool<T> currentTool;
	private BaseRenderer<T> renderer;
	@SuppressWarnings("rawtypes")
	private HashMap<Class<? extends Tool>, Tool<T>> tools = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private Class<? extends Tool> baseToolClass = null;

	/**
	 * instantiates a new editor for the given scene.
	 * 
	 * @param scene
	 *            the scene
	 */
	public Editor(@NonNull T scene) {
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
	public void addTool(@NonNull Tool<T> tool) {
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
	public T getScene() {
		return this.scene;
	}

	private void unloadCurrentTool() {
		if (currentTool != null) {
			@SuppressWarnings("rawtypes")
			ReflectionHelper<Tool> oldHelper = new ReflectionHelper<Tool>(currentTool);
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
	@SuppressWarnings("rawtypes")
	public boolean loadTool(Class<? extends Tool> toolClazz, Object... args) {
		@NonNull
		Class<? extends Tool> toolClass = (toolClazz == null) ? baseToolClass : toolClazz;
		logger.info("Load tool: " + toolClass.getSimpleName() + "[" + StringUtils.join(args, ",") + "]");
		Tool<T> next = this.tools.get(toolClass);

		if (next == null) {
			logger.warn("Tool " + toolClass.getSimpleName() + " was not found.");
			return false;
		}

		try {
			ReflectionHelper<Tool> helper = new ReflectionHelper<>(next);

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
			this.scene.setWindowTitle(toolClass.getSimpleName());

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
	public void render(Graphics g, T scene) {
		if (this.renderer != null)
			try {
				GraphicsStack stack = g.getStack();
				this.renderer.render(g, scene);
				g.setStack(stack);
			} catch (Exception e) {
				logger.error("failed to render " + currentTool.getClass().getSimpleName(), e);
			}
	}

	@Override
	public void tick(T scene) {
		if (this.currentTool != null)
			this.currentTool.tick(scene);
	}

}
