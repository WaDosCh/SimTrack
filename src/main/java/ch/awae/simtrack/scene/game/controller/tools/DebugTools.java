package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.Window;
import ch.awae.simtrack.scene.game.controller.Action;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.view.ViewPort;

public class DebugTools extends GameTool {

	enum Option {
		InputGuide,
		Coordinates,
		Reservations;
	}

	private HashMap<Option,AtomicBoolean> showing;

	private DebugToolsRenderer renderer;

	protected Model model;

	public DebugTools(Editor editor, ViewPort viewPort, Window window, Model model) {
		super(editor, false);
		this.model = model;
		
		this.showing = new HashMap<>();
		this.showing.put(Option.Coordinates, new AtomicBoolean(false));
		this.showing.put(Option.InputGuide, new AtomicBoolean(false));
		this.showing.put(Option.Reservations, new AtomicBoolean(false));
		this.renderer = new DebugToolsRenderer(showing, this, viewPort, window);

		onPress(KeyEvent.VK_F1, () -> toggle(Option.InputGuide));
		onPress(Action.DEBUG_TOOL, () -> editor.loadTool(DebugToolsView.class));

		onPress(KeyEvent.VK_F12, () -> System.exit(0));
	}

	public void toggle(Option option) {
		this.showing.get(option).set(!this.showing.get(option).get());
	}

	@Override
	public void render(Graphics graphics) {
		this.renderer.render(graphics);
	}

	public AtomicBoolean getOptionActive(Option option) {
		return this.showing.get(option);
	}

}
