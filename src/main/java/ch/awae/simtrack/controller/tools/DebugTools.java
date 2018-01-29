package ch.awae.simtrack.controller.tools;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import lombok.Getter;

public class DebugTools extends EventDrivenTool {

	enum Option {
		InputGuide,
		Coordinates;
	}

	private HashSet<Option> showing = new HashSet<Option>();

	private @Getter DebugToolsRenderer renderer = new DebugToolsRenderer(showing, this);

	public DebugTools(Editor editor) {
		super(editor, UnloadAction.IGNORE);

		onPress(KeyEvent.VK_F1, () -> toggle(Option.InputGuide));
		onPress(KeyEvent.VK_F2, () -> toggle(Option.Coordinates));
		onPress(KeyEvent.VK_F3, () -> editor.loadTool(PathFindingTool.class));
		onPress(KeyEvent.VK_F12, () -> System.exit(-1));
	}

	private void toggle(Option option) {
		if (this.showing.contains(option))
			this.showing.remove(option);
		else
			this.showing.add(option);
	}

}
