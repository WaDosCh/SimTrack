package ch.awae.simtrack.util;

import java.io.Serializable;

import lombok.Data;

public @Data final class T2<A, B> implements Serializable {
	private static final long serialVersionUID = 4760032006729811691L;

	public final A _1;
	public final B _2;

	public T2<B, A> swap() {
		return new T2<>(_2, _1);
	}

}
