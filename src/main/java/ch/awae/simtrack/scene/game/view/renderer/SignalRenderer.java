package ch.awae.simtrack.scene.game.view.renderer;

import java.awt.Color;
import java.util.Map.Entry;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.Game;
import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;

public class SignalRenderer implements Renderer {

	@Override
	public void render(Graphics g, Game view) {
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
