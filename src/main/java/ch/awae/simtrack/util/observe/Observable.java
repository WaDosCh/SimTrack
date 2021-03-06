package ch.awae.simtrack.util.observe;

public interface Observable {

	ObservableHandler getObservableHandler();

	default void register(Observer observer) {
		getObservableHandler().register(observer);
	}

	default void unregister(Observer observer) {
		getObservableHandler().unregister(observer);
	}

	default Observer createObserver() {
		Observer o = new Observer();
		register(o);
		return o;
	}

	default void notifyChanged() {
		if (getObservableHandler() != null)
			getObservableHandler().notifyObservers();
	}

}
