package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.ui.BasePanel;
import ch.awae.simtrack.core.ui.Button;
import ch.awae.simtrack.core.ui.CheckboxButton;
import ch.awae.simtrack.core.ui.Label;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.controller.Action;
import ch.awae.simtrack.scene.game.controller.tools.DebugTools.Option;

public class DebugToolsView extends GameTool {

	private BasePanel renderer;
	private DebugTools debugTools;

	public DebugToolsView(Editor<Game> editor, DebugTools debugTools) {
		super(editor, GameTool.UnloadAction.UNLOAD);
		this.debugTools = debugTools;

		this.renderer = new BasePanel(input, false);
		this.renderer.margin = 10;
		addButtons(editor.getScene());

		onPress(Action.DEBUG_TOOL, () -> editor.loadTool(FreeTool.class));
	}

	private void addButtons(Game game) {
		this.renderer.add(new Label("Debug Tools"));
		this.renderer.add(new CheckboxButton("Show coordinates", input, false, (show) -> {
			debugTools.toggle(Option.Coordinates);
		}));
		this.renderer.add(new CheckboxButton("Show reservations", input, false, (show) -> {
			debugTools.toggle(Option.Reservations);
		}));
		this.renderer.add(new Button("Pathfinding Tool", input, () -> {
			editor.loadTool(PathFindingTool.class);
		}));
		this.renderer.add(new Button("Spawn train", input, () -> {
			this.debugTools.spawnTrain();
		}));
		this.renderer.add(new CheckboxButton("Toggle grid", input, game.getDrawGrid()));
		this.renderer.add(new CheckboxButton("Pause", input, game.getIsPaused()));

	}

	@Override
	public void render(Graphics graphics, Game scene) {
		this.renderer.render(graphics, scene.getWindow());
	}

}
