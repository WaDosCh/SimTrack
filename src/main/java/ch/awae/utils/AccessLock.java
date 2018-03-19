package ch.awae.utils;

import java.util.function.Supplier;

/**
 * Access Locks encapsulate the access flag pattern where a flag is used to
 * enable or disable access to certain functions.
 * 
 * @author Andreas Wälchli
 */
public final class AccessLock {

	private volatile boolean locked;
	private final Supplier<RuntimeException> exceptionSupplier;

	public AccessLock() {
		this(false);
	}

	public AccessLock(String name) {
		this(name, false);
	}

	public AccessLock(boolean locked) {
		this(locked, () -> new IllegalStateException("access is locked"));
	}

	public AccessLock(String name, boolean locked) {
		this(locked, () -> new IllegalStateException("access to " + name + " is locked"));
	}

	public AccessLock(boolean locked, Supplier<RuntimeException> exceptionSupplier) {
		this.locked = locked;
		this.exceptionSupplier = exceptionSupplier;
	}

	public synchronized void lock() {
		locked = true;
	}

	public synchronized void unlock() {
		locked = false;
	}

	public synchronized void toggleLock() {
		locked = !locked;
	}

	public boolean isLocked() {
		return locked;
	}

	/**
	 * Tests if the access is locked.
	 * 
	 * If it is locked an exception is thrown. By default this is an
	 * {@link IllegalStateException}.
	 */
	public synchronized void test() {
		if (locked)
			throw exceptionSupplier.get();
	}

	/**
	 * Tests if the access is locked. Locks if unlocked.
	 * 
	 * If it is locked an exception is thrown. By default this is an
	 * {@link IllegalStateException}. If it is unlocked, it will be locked.
	 */
	public synchronized void testAndLock() {
		test();
		lock();
	}

}
