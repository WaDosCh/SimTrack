package ch.awae.simtrack.scene.game.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.core.input.InputController;
import ch.awae.simtrack.scene.game.controller.Editor;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.window.Graphics;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 */
public class FreeTool extends GameTool {

	private final static Stroke borderStroke = new BasicStroke(3);
	private InputController input;

	/**
	 * creates a new tool instance.
	 * 
	 * @param e the editor owning the tool
	 */
	public FreeTool(Editor editor, InputController input, ViewPortNavigator viewPort) {
		super(editor, viewPort, false);
		this.input = input;
	}

	@Override
	public void render(Graphics g) {
		TileCoordinate mouseTile = this.getMouseSceneCoordinate(this.input.getMousePosition()).toTileCoordinate();
		if (mouseTile != null) {
			g.setStroke(borderStroke);
			this.viewPort.focusHex(mouseTile, g);
			g.setColor(Color.ORANGE);
			g.drawHex();
		}
	}

}
