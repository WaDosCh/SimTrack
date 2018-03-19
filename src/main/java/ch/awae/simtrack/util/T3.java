package ch.awae.simtrack.util;

import java.io.Serializable;

import lombok.Data;

/**
 * @deprecated use {@link ch.awae.utils.T3} instead
 */
@Deprecated
public @Data class T3<A, B, C> implements Serializable {

	private static final long serialVersionUID = 314425177594806090L;
	public final A _1;
	public final B _2;
	public final C _3;

}
