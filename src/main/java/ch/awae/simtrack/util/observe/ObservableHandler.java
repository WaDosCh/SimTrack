package ch.awae.simtrack.util.observe;

import java.util.ArrayList;

public class ObservableHandler {

	private ArrayList<Observer> observers = new ArrayList<>();

	public void register(Observer observer) {
		if (!observers.contains(observer))
			observers.add(observer);
	}

	public void unregister(Observer observer) {
		observers.remove(observer);
	}

	public void notifyObservers() {
		// Log.info("notifying observers.");
		for (Observer o : observers)
			o.notifyChange();
	}

}
