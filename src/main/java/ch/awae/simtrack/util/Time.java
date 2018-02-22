package ch.awae.simtrack.util;

public class Time {
	public static boolean isOver(long timeMs) {
		return System.currentTimeMillis() >= timeMs;
	}
}
