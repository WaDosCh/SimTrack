package ch.awae.simtrack.scene.game.model.tile;

import java.awt.Color;

import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;
import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import ch.awae.simtrack.window.Graphics;
import lombok.Getter;

public class UpgradeTile implements FixedTile {

	private static final long serialVersionUID = -4319521727393669668L;

	private Tile toBeRemoved;

	private @Getter TrackTile toBeBuilt;

	public UpgradeTile(Tile toBeRemoved, TrackTile toBeBuilt) {
		this.toBeRemoved = toBeRemoved;
		this.toBeBuilt = toBeBuilt;
	}

	@Override
	public void render(TileRenderer renderer, Graphics graphics) {
		renderer.renderTrack(graphics, this.toBeBuilt, Color.gray, Color.lightGray);
		toBeRemoved.render(renderer, graphics);

		graphics.setColor(Color.yellow);
		renderer.renderTileBorder(graphics, 6);
	}

}
