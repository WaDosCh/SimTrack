package ch.awae.simtrack.controller;

import java.util.ArrayList;
import java.util.List;

import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.controller.input.Binding;
import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IViewPort;

public abstract class EventDrivenTool implements ITool, IRenderer {

	private List<Runnable> drivers = new ArrayList<>();
	protected final Input input;
	protected final Editor editor;
	protected final IController controller;
	protected final IViewPort viewPort;
	private final Binding drop;
	private final boolean autoUnload;

	public enum UnloadAction {
		UNLOAD, IGNORE;
	}

	public EventDrivenTool(Editor editor, UnloadAction action) {
		this.input = editor.getInput();
		drop = input.getBinding(Action.DROP_TOOL);
		autoUnload = action == UnloadAction.UNLOAD;
		this.editor = editor;
		this.controller = editor.getController();
		this.viewPort = this.controller.getGameView().getViewPort();
	}

	/**
	 * Registers the Runnable to be executed unconditionally on every tick
	 */
	protected void onTick(Runnable r) {
		drivers.add(r);
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the given action is pressed
	 * 
	 * @param action
	 * @param r
	 */
	protected void ifPressed(Action action, Runnable r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> binding.ifPressed(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the given action is released
	 * 
	 * @param action
	 * @param r
	 */
	protected void ifReleased(Action action, Runnable r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> binding.ifPressed(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the action is newly pressed. The edge is consumed.
	 * 
	 * @param action
	 * @param r
	 */
	protected void onPress(Action action, Runnable r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> binding.onPress(r));
	}

	/**
	 * Registers the Runnable to be executed on every tick where the key bound
	 * to the action is newly released. The edge is consumed.
	 * 
	 * @param action
	 * @param r
	 */
	protected void onRelease(Action action, Runnable r) {
		Binding binding = input.getBinding(action);
		drivers.add(() -> binding.onRelease(r));
	}

	@Override
	public void tick() {
		if (autoUnload && drop.isPressed() && drop.isEdge()) {
			editor.loadTool(null);
			return;
		}
		drivers.forEach(Runnable::run);
	}

	@Override
	public IRenderer getRenderer() {
		return this;
	}

}
