package ch.awae.simtrack.scene.game.model;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;

public class DebugOptions {

	private @Getter AtomicBoolean showCoordinates = new AtomicBoolean(false);
	private @Getter AtomicBoolean showInputGuide = new AtomicBoolean(false);
	private @Getter AtomicBoolean showTrainReservations = new AtomicBoolean(false);
	
}
