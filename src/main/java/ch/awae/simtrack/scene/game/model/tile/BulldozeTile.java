package ch.awae.simtrack.scene.game.model.tile;

import java.awt.Color;

import ch.awae.simtrack.core.Graphics;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
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
		tile.render(renderer, graphics);
		graphics.setColor(Color.red);
		graphics.rotate(45);
		graphics.fillRect(-40, -10, 80, 20);
		graphics.fillRect(-10, -40, 20, 80);
	}

}
