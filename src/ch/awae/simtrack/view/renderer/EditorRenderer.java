package ch.awae.simtrack.view.renderer;

import java.awt.Graphics2D;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.view.ARenderer;

public class EditorRenderer extends ARenderer {

	@Override
	public void render(Graphics2D g) {
		Global.editor.render(g);
	}
	

}
