package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import ch.awae.simtrack.core.BaseRenderer;
import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.window.Graphics;
import ch.awae.simtrack.window.Graphics.GraphicsStack;
import ch.awae.utils.functional.T2;

public class TileReservationRenderer implements BaseRenderer {

	private ViewPortNavigator viewPort;
	private Model model;

	public TileReservationRenderer(ViewPortNavigator viewPort, Model model) {
		this.viewPort = viewPort;
		this.model = model;
	}

	@Override
	public void render(Graphics g) {
		if (!this.model.getDebugOptions().getShowTrainReservations().get())
			return;
		GraphicsStack stack = g.getStack();
		g.push();
		HashMap<TileCoordinate, T2<Train, Integer>> tileReservations = this.model.getTileReservations();
		for (Entry<TileCoordinate, T2<Train, Integer>> t : tileReservations.entrySet()) {
			this.viewPort.focusHex(t.getKey(), g);
			g.setFont(g.getFont().deriveFont((float) 20.0));
			String txt = t.getValue()._1.getId() + "";
			for (int i = 1; i < t.getValue()._2; i++)
				txt += "+";
			int w = g.getFontMetrics().stringWidth(txt);
			int h = g.getFontMetrics().getAscent();
			g.setColor(Color.BLACK);
			g.fillRect((-w / 2) - 3, (-h / 2) - 3, w + 6, h + 6);
			g.setColor(Color.WHITE);
			g.drawString(txt, -(w / 2), (h / 2) - 1);
			g.peek();
		}
		g.setStack(stack);
	}

}
