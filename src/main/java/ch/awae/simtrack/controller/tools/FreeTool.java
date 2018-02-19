package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.SimpleEventDrivenTool;
import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.view.Graphics;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FreeTool extends SimpleEventDrivenTool {

	private final static Stroke borderStroke = new BasicStroke(6);
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	/**
	 * creates a new tool instance.
	 * 
	 * @param e
	 *            the editor owning the tool
	 */
	public FreeTool(Editor editor) {
		super(editor, UnloadAction.IGNORE);
		onPress(Action.DROP_TOOL, () -> editor.loadTool(InGameMenu.class));
	}

	@Override
	public void render(Graphics g) {
		if (mouseTile != null) {
			g.setStroke(borderStroke);
			viewPort.focusHex(mouseTile, g);
			g.setColor(Color.ORANGE);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g.rotate(angle);
			}
		}
	}

}
