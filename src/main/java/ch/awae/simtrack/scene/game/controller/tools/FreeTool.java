package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.core.Editor;
import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.core.input.InputAction;
import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.core.input.InputEvent;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 */
public class FreeTool extends GameTool {

	private final static Stroke borderStroke = new BasicStroke(3);
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));
	private InputController input;

	/**
	 * creates a new tool instance.
	 * 
	 * @param e the editor owning the tool
	 */
	public FreeTool(Editor editor, InputController input) {
		super(editor, false);
		this.input = input;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressActionAndConsume(InputAction.DROP_TOOL))
			editor.loadTool(InGameMenu.class);
		if (!event.isConsumed)
			super.handleInput(event);
	}

	@Override
	public void render(Graphics g) {
		TileCoordinate mouseTile = this.getMouseSceneCoordinate(this.input.getMousePosition()).toTileCoordinate();
		if (mouseTile != null) {
			g.setStroke(borderStroke);
			this.scene.getViewPort().focusHex(mouseTile, g);
			g.setColor(Color.ORANGE);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g.rotate(angle);
			}
		}
	}

}
