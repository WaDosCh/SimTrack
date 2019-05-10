package ch.awae.simtrack.scene.game.model.tile;

import ch.awae.simtrack.scene.game.view.renderer.TileRenderer;
import ch.awae.simtrack.window.Graphics;

public class EnvironmentTile implements FixedTile {

	public static final EnvironmentTile WATER_TILE = new EnvironmentTile(EnvironmentType.WATER);

	public enum EnvironmentType {
		WATER
	}

	private static final long serialVersionUID = -5815733376516444057L;

	protected EnvironmentType type;

	protected EnvironmentTile(EnvironmentType type) {
		this.type = type;
	}

	public EnvironmentType getType() {
		return this.type;
	}

	@Override
	public void render(TileRenderer renderer, Graphics graphics) {
		renderer.renderWater(graphics);
	}
}
