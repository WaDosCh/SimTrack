package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.view.IGameView;
import lombok.Getter;

public class DebugTools {

	enum Option {
		InputGuide, Coordinates;
	}

	private Keyboard keyboard;
	@Getter
	private DebugToolsRenderer renderer;
	private HashSet<Option> showing;

	public DebugTools(Keyboard keyboard, Mouse mouse, IGameView gameView) {
		this.keyboard = keyboard;
		this.showing = new HashSet<Option>();
		this.renderer = new DebugToolsRenderer(this.showing, mouse, gameView);
	}

	public void tick() {
		if (this.keyboard.key(KeyEvent.VK_F1))
			toggle(Option.InputGuide);
		if (this.keyboard.key(KeyEvent.VK_F2))
			toggle(Option.Coordinates);
		if (this.keyboard.key(KeyEvent.VK_F12))
			System.exit(0);
	}

	private void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

}
