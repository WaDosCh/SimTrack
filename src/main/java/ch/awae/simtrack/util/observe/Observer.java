package ch.awae.simtrack.util.observe;

public class Observer {

	private volatile boolean changed = true;

	public synchronized boolean isChanged() {
		if (changed) {
			changed = false;
			return true;
		}
		return false;
	}

	public void ifChanged(Runnable runner) {
		if (this.isChanged()) {
			runner.run();
		}
	}

	public synchronized void notifyChange() {
		changed = true;
	}

}
