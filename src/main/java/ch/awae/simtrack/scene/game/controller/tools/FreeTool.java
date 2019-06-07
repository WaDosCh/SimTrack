package ch.awae.simtrack.scene.game.controller.tools;

import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.window.Graphics;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 */
public class FreeTool extends GameTool {

	private InputController input;
	private Model model;

	/**
	 * creates a new tool instance.
	 * 
	 * @param model
	 * 
	 * @param e the editor owning the tool
	 */
	public FreeTool(Editor editor, InputController input, ViewPortNavigator viewPort, Model model) {
		super(editor, viewPort);
		this.input = input;
		this.model = model;
	}

	@Override
	public void render(Graphics g) {
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.SELECT)) {
			TileCoordinate selectTile = this.getMouseSceneCoordinate(event.getCurrentMousePosition())
					.toTileCoordinate();
			this.model.select(selectTile);
			return;
		}
		if (event.isPressAction(InputAction.DESELECT)) {
			if (this.model.hasSelected()) {
				event.consume();
				this.model.select(null);
				return;
			}
		}
	}

}
