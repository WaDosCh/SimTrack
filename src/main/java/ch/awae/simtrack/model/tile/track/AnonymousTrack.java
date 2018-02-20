package ch.awae.simtrack.model.tile.track;

import java.io.ObjectStreamException;

import ch.awae.simtrack.model.position.TilePath;
import ch.awae.simtrack.model.tile.BasicTrackTile;
import ch.awae.simtrack.model.tile.TrackTile;
import ch.awae.simtrack.util.serial.SerializationProxy;
import lombok.Getter;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2014-01-26
 * @since SimTrack 0.2.2
 */
class AnonymousTrack extends BasicTrackTile implements SerializationProxy {

	private static final long serialVersionUID = 1078822789992674066L;
	private float cost;
	@Getter
	private TilePath[] railPaths;

	AnonymousTrack(TilePath[] connections, float cost) {
		this.railPaths = connections.clone();
		TrackValidator.sortPathList(this.railPaths);
		this.cost = cost;
	}

	@Override
	public float getTravelCost() {
		return this.cost;
	}

	public static TrackTile from(TrackTile tile) {
		return new AnonymousTrack(tile.getRailPaths(), tile.getTravelCost());
	}

	public Object readResolve() throws ObjectStreamException {
		return TrackValidator.intern(this);
	}

}
