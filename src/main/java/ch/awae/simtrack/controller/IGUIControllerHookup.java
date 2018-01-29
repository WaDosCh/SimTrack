package ch.awae.simtrack.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.function.Consumer;

/**
 * The GUI controller hook-up used for the observer initialisation.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public interface IGUIControllerHookup {

	/**
	 * provides a consumer that registers a given mouse adapter to the required
	 * event hooks
	 * 
	 * @return the mouse hook-up method
	 */
	public Consumer<MouseAdapter> getMouseHookup();

	/**
	 * provides a consumer that registers a given key adapter to the required
	 * event hooks
	 * 
	 * @return the keyboard hook-up method
	 */
	public Consumer<KeyAdapter> getKeyboardHookup();

	public Consumer<String> getWindowTitleHookup();

}
