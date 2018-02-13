package ch.awae.simtrack.controller;

public class HighPrecisionClock {

	private static final int busyWaitAheadNS = 1000000;

	private int fps;
	private Runnable tick;
	private boolean running;
	private Thread thread;
	private String name;

	public HighPrecisionClock(int fps, Runnable tick, String name) {
		this.fps = fps;
		this.tick = tick;
		this.name = name;
	}

	public void start() {
		if (!this.running) {
			this.running = true;
			this.thread = new Thread(this::run, "HighPrecisionClock (" + this.name + ")");
			this.thread.setPriority(Thread.MAX_PRIORITY);
			this.thread.start();
		}
	}

	public void stop() {
		this.running = false;
	}

	private void run() {
		long delayBetweenFrameNS = 1000000000 / fps;
		long lastFrameTimeNS = 0;
		while (this.running) {
			long currentTimeNS = System.nanoTime();
			if (currentTimeNS - lastFrameTimeNS >= delayBetweenFrameNS) {
				lastFrameTimeNS = currentTimeNS;
				try {
					this.tick.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				long remaining = lastFrameTimeNS + delayBetweenFrameNS - System.nanoTime() - busyWaitAheadNS;
				if (remaining > 0)
					Thread.sleep(remaining / 1000000, (int) (remaining % 1000000));
			} catch (InterruptedException e) {
			}
		}
	}

}
