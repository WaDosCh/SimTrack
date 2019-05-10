package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import ch.awae.simtrack.scene.game.controller.ViewPortNavigator;
import ch.awae.simtrack.scene.game.model.Model;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.window.Graphics;
import ch.awae.utils.functional.T2;

public class SignalRenderer implements Renderer {
	
	private Model model;
	private ViewPortNavigator viewPort;

	public SignalRenderer(ViewPortNavigator viewPort, Model model) {
		this.viewPort = viewPort;
		this.model = model;
	}

	@Override
	public void render(Graphics g) {
		g.push();

		for (Entry<TileEdgeCoordinate, Signal> entry : this.model.getSignals()) {
			TileEdgeCoordinate position = entry.getKey();
			Signal signal = entry.getValue();

			g.peek();
			this.viewPort.focusHex(position.tile, g);
			g.rotate(position.getEdge().getAngleOut());
			g.setColor(Color.black);
			if (signal.getType() == Signal.Type.ONE_WAY)
				g.fillRect(35, 14, 12, 12);
			else
				g.fillOval(35, 14, 12, 12);

			g.setColor(Color.RED);

			HashMap<TileCoordinate, T2<Train, Integer>> reservations = this.model.getTileReservations();
			T2<Train, Integer> before = reservations.get(position.tile);
			T2<Train, Integer> after = reservations.get(position.getOppositeDirection().tile);
			if (before != null && after != null && before._1.equals(after._1))
				g.setColor(Color.GREEN);

			g.fillOval(37, 16, 8, 8);
		}
		g.pop();
	}

}
