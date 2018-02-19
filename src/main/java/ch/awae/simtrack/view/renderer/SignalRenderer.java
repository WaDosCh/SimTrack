package ch.awae.simtrack.view.renderer;

import java.awt.Color;
import java.util.Map.Entry;

import ch.awae.simtrack.model.entity.Signal;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.view.Graphics;
import ch.awae.simtrack.view.IGameView;

public class SignalRenderer implements IRenderer {

	@Override
	public void render(Graphics g, IGameView view) {
		g.push();

		for (Entry<TileEdgeCoordinate, Signal> entry : view.getModel().getSignals()) {
			TileEdgeCoordinate position = entry.getKey();
			Signal signal = entry.getValue();

			g.peek();
			view.getViewPort().focusHex(position.tile, g);
			if (signal.getType() == Signal.Type.ONE_WAY)
				g.setColor(Color.PINK);
			else
				g.setColor(Color.MAGENTA);
			g.rotate(position.getEdge().getAngleOut());
			g.fillOval(41, 8, 18, 18);
		}
	}

}
