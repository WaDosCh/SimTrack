package ch.awae.simtrack.scene.game.model;

import lombok.Getter;

public class GameClock {

	private @Getter boolean running = true;
	private double scale = 1.0;
	private double time = 0;
	private double tickDuration = 0;

	public void tick(long millis) {
		if (running) {
			tickDuration = millis * scale;
			time += tickDuration;
		} else {
			tickDuration = 0;
		}
	}

	/**
	 * resume the clock. if the scale set to 0 it will be reset to 1
	 */
	public void resume() {
		running = true;
		if (scale == 0)
			scale = 1;
	}

	/**
	 * Pause the clock
	 */
	public void pause() {
		running = false;
	}

	/**
	 * set a new game speed. it the clock is currently paused, it will be
	 * resumed at that speed
	 */
	public void setScale(double scale) {
		running = true;
		this.scale = scale;
	}

	/**
	 * the speed at which the clock moves (relative to real-life)
	 * 
	 * if paused this may still provide a non-zero value
	 */
	public double getScale() {
		return scale;
	}

	/*
	 * the total time passed in the game.
	 * 
	 * the milliseconds should remain accurate for approximately 316'888 years
	 * (so it probably is fine :P)
	 */
	public double getTime() {
		return time;
	}

	/**
	 * The time passed in the game since the last tick
	 */
	public double getTickDuration() {
		return tickDuration;
	}

}
