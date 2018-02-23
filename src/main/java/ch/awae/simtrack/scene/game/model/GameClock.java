package ch.awae.simtrack.scene.game.model;

import lombok.Getter;

public class GameClock {

	private @Getter boolean running = true;
	private @Getter double scale = 1.0;
	private @Getter double time = 0;
	private @Getter double tickDuration = 0;

	public void tick(long millis) {
		if (running) {
			tickDuration = millis * scale;
			time += tickDuration;
		}
	}

	public void resume() {
		if (running && scale == 0)
			scale = 1;
		else
			running = true;
	}

	public void pause() {
		running = false;
	}

	public void setScale(double scale) {
		running = true;
		this.scale = scale;
	}

}
