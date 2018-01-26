package ch.awae.simtrack.util;

public final class T2<A, B> {

	public final A _1;
	public final B _2;

	public T2(A _1, B _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public T2<B, A> swap() {
		return new T2<>(_2, _1);
	}

}
