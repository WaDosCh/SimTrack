package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Action;

public class DebugTools extends GameTool {

	enum Option {
		InputGuide,
		Coordinates,
		Reservations;
	}

	private HashSet<Option> showing = new HashSet<Option>();

	private DebugToolsRenderer renderer = new DebugToolsRenderer(showing, this);

	public DebugTools(Editor<Game> editor) {
		super(editor, GameTool.UnloadAction.IGNORE);

		onPress(KeyEvent.VK_F1, () -> toggle(Option.InputGuide));
		onPress(Action.DEBUG_TOOL, () -> editor.loadTool(DebugToolsView.class));

		onPress(KeyEvent.VK_F12, () -> System.exit(0));
	}

	public void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

	@Override
	public void tick(Game scene) {
		super.tick(scene);
	}

	@Override
	public void render(Graphics graphics, Game scene) {
		this.renderer.render(graphics, scene);
	}

}
