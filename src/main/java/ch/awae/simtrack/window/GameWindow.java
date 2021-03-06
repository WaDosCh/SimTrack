package ch.awae.simtrack.window;

import java.awt.Color;

import ch.awae.simtrack.core.Window;

public interface GameWindow extends Window {

	void discard();

	Graphics getGraphics();

	void flipFrame();

	/**
	 * indicates if the window has been resized since the last call to this method. By default this always returns false
	 * (i.e. resize not supported)
	 */
	default boolean resized() {
		return false;
	}

	public static final Color BG_COLOR = new Color(238, 238, 238);

}