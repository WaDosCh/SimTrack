package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import ch.awae.simtrack.controller.input.Keyboard;
import lombok.Getter;

public class DebugTools {

	enum Option {
		InputGuide, Coordinates;
	}

	private Keyboard keyboard;
	@Getter
	private DebugToolsRenderer renderer;
	private HashSet<Option> showing;

	public DebugTools(Keyboard keyboard) {
		this.keyboard = keyboard;
		this.showing = new HashSet<Option>();
		this.renderer = new DebugToolsRenderer(this.showing);
	}

	public void tick() {
		if (this.keyboard.key(KeyEvent.VK_F1))
			toggle(Option.InputGuide);
		if (this.keyboard.key(KeyEvent.VK_F2))
			toggle(Option.Coordinates);
	}

	private void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

}
