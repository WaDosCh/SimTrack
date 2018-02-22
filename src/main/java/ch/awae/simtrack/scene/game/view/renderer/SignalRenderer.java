package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Train;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.util.T2;

public class SignalRenderer implements Renderer {

	@Override
	public void render(Graphics g, Game view) {
		g.push();

		for (Entry<TileEdgeCoordinate, Signal> entry : view.getModel().getSignals()) {
			TileEdgeCoordinate position = entry.getKey();
			Signal signal = entry.getValue();

			g.peek();
			view.getViewPort().focusHex(position.tile, g);
			g.rotate(position.getEdge().getAngleOut());
			g.setColor(Color.black);
			if (signal.getType() == Signal.Type.ONE_WAY)
				g.fillRect(35, 14, 12, 12);
			else
				g.fillOval(35, 14, 12, 12);

			g.setColor(Color.RED);

			HashMap<TileCoordinate, T2<Train, Integer>> reservations = view.getModel().getTileReservations();
			T2<Train, Integer> before = reservations.get(position.tile);
			T2<Train, Integer> after = reservations.get(position.getOppositeDirection().tile);
			if (before != null && after != null && before._1.equals(after._1))
				g.setColor(Color.GREEN);

			g.fillOval(37, 16, 8, 8);
		}
	}

}
