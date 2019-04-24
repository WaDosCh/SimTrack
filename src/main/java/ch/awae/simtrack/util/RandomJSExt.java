package ch.awae.simtrack.util;

import ch.judos.generic.data.RandomJS;

/**
 * temporary extensions to RandomJS, these will be available with next version of WaDoSUtil lib.<br>
 * XXX: use WaDosUtil-1.4.9 to get rid of methods here
 */
public class RandomJSExt {

	public static <T extends Enum<T>> T getEnum(Class<T> clazz) {
		try {
			@SuppressWarnings("unchecked")
			T[] values = (T[]) clazz.getMethod("values").invoke(null);
			return RandomJS.getObject(values);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
