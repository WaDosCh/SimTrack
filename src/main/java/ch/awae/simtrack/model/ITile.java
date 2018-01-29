package ch.awae.simtrack.model;

import java.io.Serializable;

/**
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-24
 * @since SimTrack 0.2.2 (0.2.1)
 */
public interface ITile extends IEntity, Serializable {

	default TileType getType() {
		return TileType.UNKNOWN;
	}

}
