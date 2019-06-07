package ch.awae.simtrack.scene.game.controller.event;

public interface EventListener {

	public void eventOccured(EventType type, Object... args);

}
