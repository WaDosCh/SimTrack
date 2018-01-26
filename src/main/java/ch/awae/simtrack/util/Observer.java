package ch.awae.simtrack.util;

public class Observer {

	private volatile boolean changed = true;

	public synchronized boolean isChanged() {
		if (changed) {
			changed = false;
			return true;
		}
		return false;
	}
	
	public synchronized void notifyChange() {
		changed = true;
	}

}
