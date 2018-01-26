package ch.awae.simtrack.controller.input;

public interface Trigger {

	boolean test();

	default void test(Runnable r) {
		if (test())
			r.run();
	}

	default void ifActive(Runnable r) {
		test(r);
	}

	static public enum Direction {
		ACTIVATE, DEACTIVATE;
	}

}
