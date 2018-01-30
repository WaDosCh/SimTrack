package ch.awae.simtrack.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CollectionUtil {

	private static Random random = new Random(System.currentTimeMillis());

	public static <K> K randomValue(Collection<K> set) {
		int index = random.nextInt(set.size());
		Iterator<K> iterator = set.iterator();
		for (int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	public static <K> K randomValue(List<K> list) {
		return list.get(random.nextInt(list.size()));
	}

}
