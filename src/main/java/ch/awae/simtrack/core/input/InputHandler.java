package ch.awae.simtrack.core.input;

public interface InputHandler {

	/**
	 * test if you should, can and want handle the given inputEvent. If yes call consume() on it.
	 * 
	 * @param event
	 */
	public void handleInput(InputEvent event);

	public default void unfocus() {
	}
}
