package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.Edge;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class TilePath implements Comparable<TilePath> {

	public final Edge _1, _2;

	public TilePath(Edge _1, Edge _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public TilePath normalised() {
		if (_1.ordinal() > _2.ordinal())
			return new TilePath(_2, _1);
		else
			return this;
	}

	@Override
	public int compareTo(TilePath path) {
		int high = _1.compareTo(path._1);
		if (high == 0)
			return _2.compareTo(path._2);
		else
			return high;
	}

	public TilePath swap() {
		return new TilePath(_2, _1);
	}

}
