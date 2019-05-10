package ch.awae.simtrack.scene.game.model.tile;

import java.awt.Color;
import java.awt.Graphics2D;

import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import ch.awae.simtrack.window.Graphics;
import lombok.Getter;

/**
 * tile marked for destruction which can not be removed instantly due to tile reservations
 */
public class BulldozeTile implements FixedTile {

	private static final long serialVersionUID = -4319521727393669668L;

	private @Getter Tile tile;

	public BulldozeTile(Tile toBeRemoved) {
		this.tile = toBeRemoved;
	}

	@Override
	public void render(TileRenderer renderer, Graphics graphics) {
		graphics.push();
		tile.render(renderer, graphics);
		graphics.pop();
		graphics.setColor(Color.red);
		((Graphics2D) graphics).rotate(Math.PI / 4);
		graphics.fillRect(-40, -10, 80, 20);
		graphics.fillRect(-10, -40, 20, 80);
	}

}
