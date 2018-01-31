package ch.awae.simtrack.model.track;

import ch.awae.simtrack.model.position.TilePath;
import ch.awae.simtrack.model.tile.BasicTrackTile;
import lombok.Getter;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2014-01-26
 * @since SimTrack 0.2.2
 */
class AnonymousTrack extends BasicTrackTile {

	private static final long serialVersionUID = 1078822789992674066L;
	private float cost;
	@Getter
	private TilePath[] railPaths;

	AnonymousTrack(TilePath[] connections, float cost) {
		this.railPaths = connections.clone();
		this.cost = cost;
	}

	@Override
	public float getTravelCost() {
		return this.cost;
	}

}
