package ch.awae.simtrack.controller.input;

import ch.awae.simtrack.controller.Log;
import lombok.Getter;

public class Binding {

	private int counter = 0;
	private @Getter boolean edge = false;

	/**
	 * Updates the internal state
	 * 
	 * @param state
	 */
	public void update(boolean state) {
		edge = ((counter > 0) != state);
		if (state)
			counter++;
		else if (counter > 0)
			counter--;
	}

	public boolean isPressed() {
		return counter > 0;
	}

	/**
	 * Consumes an edge
	 */
	public void consume() {
		this.edge = false;
	}

	/**
	 * If currently pressed, execute the runnable. A potential edge is not
	 * consumed.
	 * 
	 * @param r
	 */
	public void ifPressed(Runnable r) {
		if (counter > 0)
			r.run();
	}

	/**
	 * If currently released, execute the runnable. A potential edge is not
	 * consumed.
	 * 
	 * @param r
	 */
	public void ifReleased(Runnable r) {
		if (counter == 0)
			r.run();
	}

	/**
	 * If a rising edge is present, execute the runnable and consume the edge
	 * 
	 * @param r
	 */
	public void onPress(EdgeProcessor r) {
		if ((counter > 0) && edge) {
			try {
				r.run();
				consume();
			} catch (SkipConsumeException e) {
				Log.info("skipping consume");
			}
		}
	}

	/**
	 * If a falling edge is present, execute the runnable and consume the edge
	 * 
	 * @param r
	 */
	public void onRelease(EdgeProcessor r) {
		if ((counter == 0) && edge) {
			try {
				r.run();
				consume();
			} catch (SkipConsumeException e) {
				Log.info("skipping consume");
			}
		}
	}

	public static void skipConsume() throws SkipConsumeException {
		throw new SkipConsumeException();
	}

	// ######## EXCEPTION TYPE ########
	public static class SkipConsumeException extends Exception {
		private static final long serialVersionUID = 7232893320426249523L;
	}

	@FunctionalInterface
	public static interface EdgeProcessor {
		void run() throws SkipConsumeException;
	}

}
