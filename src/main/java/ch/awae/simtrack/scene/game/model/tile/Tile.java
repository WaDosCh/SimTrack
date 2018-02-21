package ch.awae.simtrack.scene.game.model.tile;

import java.io.Serializable;

/**
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-24
 * @since SimTrack 0.2.2 (0.2.1)
 */
public interface Tile extends Serializable {

	default TileType getType() {
		return TileType.UNKNOWN;
	}

}
