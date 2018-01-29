package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.Edge;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public abstract class BasicTrackTile implements ITrackTile {

	private static final long serialVersionUID = 3482264021238974251L;

	@Override
	public boolean connectsAt(Edge edge) {
		TilePath[] paths = this.getRailPaths();
		for (TilePath p : paths)
			if (p._1 == edge || p._2 == edge)
				return true;
		return false;
	}

	@Override
	public TilePath[] getPaths() {
		TilePath[] raw = getRailPaths();
		TilePath[] res = new TilePath[2 * raw.length];
		for (int i = 0; i < raw.length; i++) {
			res[2 * i] = raw[i];
			res[2 * i + 1] = raw[i].swap();
		}
		return res;
	}
}
