package ch.awae.simtrack.core;

public interface RootWindow extends Window {

	void init(Input input);

	void discard();

	Graphics getGraphics();

	void flipFrame();

	/**
	 * indicates if the window has been resized since the last call to this
	 * method. By default this always returns false (i.e. resize not supported)
	 */
	default boolean resized() {
		return false;
	}

}
