package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;
import java.io.*;

import ch.awae.simtrack.controller.IController;
import ch.awae.simtrack.controller.IEditor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Keyboard.KeyTrigger;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.input.Trigger.Direction;
import ch.awae.simtrack.model.IModel;
import ch.awae.simtrack.view.IRenderer;

public class InGameMenu implements ITool {

	private Mouse mouse;
	private Keyboard keyboard;
	private IEditor editor;
	private KeyTrigger esc;
	private InGameMenuRenderer renderer;
	private IModel model;
	private IController controller;

	public InGameMenu(Mouse mouse, Keyboard keyboard, IEditor editor, IModel model,
		IController controller) {
		this.mouse = mouse;
		this.keyboard = keyboard;
		this.editor = editor;
		this.model = model;
		this.controller = controller;
		this.esc = this.keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_ESCAPE);
		this.renderer = new InGameMenuRenderer(this.mouse);
		this.renderer.addButton("Resume", this::resume);
		this.renderer.addButton("Save", this::save);
		this.renderer.addButton("Load", this::load);
		this.renderer.addButton("Quit Game", () -> System.exit(0));
	}

	private void save() {
		try {
			new File("saves/").mkdir();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(
				"saves/map1.simtrack.save")));
			out.writeObject(this.model);
			out.close();
			this.editor.loadTool(FreeTool.class);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(
				"saves/map1.simtrack.save")));
			IModel model = (IModel) in.readObject();
			in.close();
			this.controller.loadModel(model);
			this.editor.loadTool(FreeTool.class);
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void resume() {
		this.editor.loadTool(FreeTool.class);
	}

	@Override
	public IRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
	}

	@Override
	public void tick() {
		if (this.esc.test()) {
			this.editor.loadTool(FreeTool.class);
		}
	}

	public void setModel(IModel model) {
		this.model = model;
	}

}
