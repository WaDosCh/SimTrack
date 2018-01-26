package ch.awae.simtrack.util;

public final class Tuple<A, B> {

	public final A _1;
	public final B _2;

	public Tuple(A _1, B _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public Tuple<B, A> swap() {
		return new Tuple<>(_2, _1);
	}

}
