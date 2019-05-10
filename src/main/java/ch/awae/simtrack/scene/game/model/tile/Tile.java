package ch.awae.simtrack.scene.game.model.tile;

import java.io.Serializable;

import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import ch.awae.simtrack.window.Graphics;

public interface Tile extends Serializable {

	public void render(TileRenderer renderer, Graphics graphics);

}
