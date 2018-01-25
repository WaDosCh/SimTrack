package ch.awae.simtrack.controller.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;

import ch.awae.simtrack.controller.tools.DebugTools.Option;
import ch.awae.simtrack.util.Resource;
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.IView;
import ch.awae.simtrack.view.ViewConstants;

public class DebugToolsRenderer implements IRenderer {

	private HashSet<Option> showing;
	private String[] inputGuideText;

	public DebugToolsRenderer(HashSet<Option> showing) {
		this.showing = showing;
		this.inputGuideText = Resource.getText("inputKeys.txt");
	}

	@Override
	public void render(Graphics2D g, IView view) {
		if (this.showing.contains(Option.InputGuide)) {
			int w = view.getHorizontalScreenSize();
			int h = view.getVerticalScreenSize();
			g.setColor(new Color(255, 255, 255, 225));
			g.fillRect(50, 50, w - 100, h - 100 - ViewConstants.toolbarHeight);
			g.setColor(new Color(128, 128, 128, 255));
			g.drawRect(50, 50, w - 100, h - 100 - ViewConstants.toolbarHeight);

			int y = 80;
			g.setColor(Color.black);
			g.setFont(ViewConstants.text);
			for (String s : this.inputGuideText) {
				g.drawString(s, 60, y);
				y += ViewConstants.text.getSize();
			}
		}
	}

}
