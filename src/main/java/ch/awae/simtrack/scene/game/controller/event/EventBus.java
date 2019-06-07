package ch.awae.simtrack.scene.game.controller.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.judos.generic.data.HashMapSet;

public class EventBus {

	private HashMapSet<EventType, EventListener> listeners;
	private AtomicBoolean lockModify = new AtomicBoolean(false);
	private List<Runnable> executeLater = new ArrayList<>();

	public EventBus() {
		this.listeners = new HashMapSet<EventType, EventListener>();
	}

	public void subscribe(EventType type, EventListener listener) {
		execute(() -> {
			this.listeners.put(type, listener);
		});
	}

	public void unsubscribe(EventType type, EventListener listener) {
		execute(() -> {
			this.listeners.getSet(type).remove(listener);
		});
	}

	public void unsubscribe(EventListener listener) {
		execute(() -> {
			this.listeners.removeValue(listener);
		});
	}

	public void notifyEvent(EventType type, Object... args) {
		this.lockModify.set(true);
		if (this.listeners.getSet(type) != null) {
			for (EventListener listener : this.listeners.getSet(type)) {
				listener.eventOccured(type, args);
			}
		}
		this.executeLater.forEach(Runnable::run);
		this.executeLater.clear();
		this.lockModify.set(false);
	}

	/**
	 * used such that EventListeners can unsubscribe while an event is still published (no
	 * ConcurrentModificationException), not multi-thread safe, but enough for one thread without exception
	 */
	private void execute(Runnable modification) {
		if (this.lockModify.get()) {
			this.executeLater.add(modification);
		} else
			modification.run();
	}
}
