package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;

import ch.awae.simtrack.controller.IEditor;
import ch.awae.simtrack.controller.ITool;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Keyboard.KeyTrigger;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.controller.input.Trigger.Direction;
import ch.awae.simtrack.view.IRenderer;

public class InGameMenu implements ITool {

	private Mouse mouse;
	private Keyboard keyboard;
	private IEditor editor;
	private KeyTrigger esc;
	private InGameMenuRenderer renderer;

	public InGameMenu(Mouse mouse, Keyboard keyboard, IEditor editor) {
		this.mouse = mouse;
		this.keyboard = keyboard;
		this.editor = editor;
		this.esc = this.keyboard.trigger(Direction.ACTIVATE,
				KeyEvent.VK_ESCAPE);
		this.renderer = new InGameMenuRenderer(this.mouse);
		this.renderer.addButton("Resume", this::resume);
		this.renderer.addButton("Save", () -> {
		});
		this.renderer.addButton("Load", () -> {
		});
		this.renderer.addButton("Quit Game", () -> System.exit(0));
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

}
