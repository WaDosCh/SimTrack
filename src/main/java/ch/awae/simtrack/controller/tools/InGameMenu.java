package ch.awae.simtrack.controller.tools;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import lombok.Getter;

public class InGameMenu extends EventDrivenTool {

    private @Getter InGameMenuRenderer renderer;

    public InGameMenu(Editor editor) {
        super(editor, UnloadAction.UNLOAD);

		this.renderer = new InGameMenuRenderer(input);
		this.renderer.addButton("Resume", () -> editor.loadTool(FreeTool.class));
		this.renderer.addButton("Save", () -> {
		});
		this.renderer.addButton("Load", () -> {
		});
		this.renderer.addButton("Quit Game", () -> System.exit(0));
	}

}
