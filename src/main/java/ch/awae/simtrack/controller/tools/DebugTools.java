package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Keyboard.Direction;
import ch.awae.simtrack.controller.input.Keyboard.KeyTrigger;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.view.IGameView;
import lombok.Getter;

public class DebugTools {

	enum Option {
		InputGuide, Coordinates;
	}

	private KeyTrigger F1, F2, F12;
	
	@Getter
	private DebugToolsRenderer renderer;
	private HashSet<Option> showing;

	public DebugTools(Keyboard keyboard, Mouse mouse, IGameView gameView) {
		F1 = keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_F1);
		F2 = keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_F2);
		F12 = keyboard.trigger(Direction.ACTIVATE, KeyEvent.VK_F12);
		
		this.showing = new HashSet<Option>();
		this.renderer = new DebugToolsRenderer(this.showing, mouse, gameView);
	}

	public void tick() {
		if (F1.test())
			toggle(Option.InputGuide);
		if (F2.test())
			toggle(Option.Coordinates);
		if (F12.test())
			System.exit(0);
	}

	private void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

}
