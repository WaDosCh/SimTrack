package ch.awae.simtrack.controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.controller.input.Action;
import ch.awae.simtrack.view.IGameView;
import ch.awae.simtrack.view.IRenderer;
import lombok.Getter;

/**
 * "Free-Hand" tool. This tool will be used for in-situ tile manipulation
 * 
 * @author Andreas Wälchli
 * @version 1.4, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class FreeTool extends EventDrivenTool implements IRenderer {

	private final static Stroke borderStroke = new BasicStroke(6);
	private final static int hexSideHalf = (int) (50 / Math.sqrt(3));

	private @Getter IRenderer renderer = this;

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
	public void render(Graphics2D g, IGameView view) {
		if (mouseTile != null) {
			g.setStroke(borderStroke);
			Graphics2D g2 = view.getViewPort().focusHex(mouseTile, g);
			g2.setColor(Color.ORANGE);
			double angle = Math.PI / 3;
			for (int i = 0; i < 6; i++) {
				g2.drawLine(50, -hexSideHalf, 50, hexSideHalf);
				g2.rotate(angle);
			}
		}
	}

}
