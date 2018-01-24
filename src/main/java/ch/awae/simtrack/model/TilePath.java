package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.Edge;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_1 == null) ? 0 : _1.hashCode());
		result = prime * result + ((_2 == null) ? 0 : _2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TilePath)) {
			return false;
		}
		TilePath other = (TilePath) obj;
		if (_1 != other._1) {
			return false;
		}
		if (_2 != other._2) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(TilePath path) {
		int high = _1.compareTo(path._1);
		if (high == 0)
			return _2.compareTo(path._2);
		else
			return high;
	}

}
