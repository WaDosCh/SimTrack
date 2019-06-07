package ch.awae.simtrack.scene.game.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.core.input.InputHandler;
import ch.awae.simtrack.scene.game.controller.tools.BuildTool;
import ch.awae.simtrack.scene.game.controller.tools.FreeTool;
import ch.awae.simtrack.scene.game.controller.tools.PathFindingTool;
import ch.awae.simtrack.scene.game.controller.tools.SignalTool;
import ch.awae.simtrack.scene.game.controller.tools.Tool;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.window.Graphics;
import ch.awae.simtrack.window.Graphics.GraphicsStack;
import lombok.NonNull;

/**
 * top-level management of the active side of the user interface. It manages the activation of the different editor
 * tools. It thereby targets reduction of complexity by delegating the user actions to well-defined tools.
 */
public class Editor implements BaseTicker, BaseRenderer, InputHandler {

	private Logger logger = LogManager.getLogger(getClass());
	private final static Stroke borderStroke = new BasicStroke(3);

	private Model model;
	private ViewPortNavigator viewPort;
	private InputController input;
	private PathFinding pathfinder;

	private Tool currentTool;
	private HashMap<Class<? extends Tool>, Tool> tools = new HashMap<>();

	/**
	 * instantiates a new editor for the given scene.
	 */
	public Editor(Model model, ViewPortNavigator viewPortNavigator, InputController input, PathFinding pathfinder) {
		this.model = model;
		this.viewPort = viewPortNavigator;
		this.input = input;
		this.pathfinder = pathfinder;

		createTools();
	}

	private void createTools() {
		FreeTool freeTool = new FreeTool(this, input, this.viewPort, this.model);
		this.currentTool = freeTool;
		addTool(freeTool);
		addTool(new BuildTool(this, this.model, this.input, this.viewPort));
		addTool(new PathFindingTool(this, this.viewPort, this.pathfinder));
		addTool(new SignalTool(this, this.model, this.input, this.viewPort));
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
	private void addTool(@NonNull Tool tool) {
		this.tools.put(tool.getClass(), tool);
	}

	/**
	 * loads a tool and unloads the current one. If the new tool cannot be loaded, the current one will not be unloaded
	 * and stays active.
	 * 
	 * @param name the identifier string of the new tool
	 * @param args additional arguments to hand over to the new tool
	 * @return {@code true} if the tool switch was successful
	 */
	public void loadTool(Class<? extends Tool> toolClazz, Object... args) {
		if (toolClazz == null)
			throw new RuntimeException("May not load null tool");
		logger.debug("Load tool: " + toolClazz.getSimpleName() + "[" + StringUtils.join(args, ",") + "]");
		Tool next = this.tools.get(toolClazz);
		if (next == null) {
			throw new RuntimeException("Tool " + toolClazz.getSimpleName() + " was not registered");
		}
		if (this.currentTool != next) {
			this.currentTool.unloadTool();
			next.loadTool(args);
			this.currentTool = next;
		}
	}

	/**
	 * renders the currently active tool
	 * 
	 * @param g the graphics instance to render onto
	 */
	@Override
	public void render(Graphics g) {
		if (this.model.getSelectedTile() != null) {
			g.push();
			g.setStroke(borderStroke);
			this.viewPort.focusHex(this.model.getSelectedTile(), g);
			g.setColor(Color.ORANGE);
			g.drawHex();
			g.pop();
		}
		if (this.currentTool != null) {
			GraphicsStack stack = g.getStack();
			this.currentTool.render(g);
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
