package ch.awae.simtrack.core.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.awae.utils.logic.Logic;
import lombok.Getter;

public class Binding implements Logic {

	private static Logger logger = LogManager.getLogger(Binding.class);
	
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
		else
			counter = 0;
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
				logger.debug("skipping consume");
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
				logger.debug("skipping consume");
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

	@Override
	public boolean evaluate() {
		return isPressed();
	}

	@Override
	public Logic edge() {
		return () -> {
			// TODO: fix
			boolean e = isPressed() && isEdge();
			consume();
			return e;
		};
	}
	
}
