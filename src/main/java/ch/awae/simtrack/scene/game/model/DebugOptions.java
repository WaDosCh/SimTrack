package ch.awae.simtrack.scene.game.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;

public class DebugOptions implements Serializable {

	private static final long serialVersionUID = 2661285352490036705L;

	private @Getter AtomicBoolean showCoordinates = new AtomicBoolean(false);
	private @Getter AtomicBoolean showInputGuide = new AtomicBoolean(false);
	private @Getter AtomicBoolean showTrainReservations = new AtomicBoolean(false);
	private @Getter AtomicBoolean renderSoftware = new AtomicBoolean(false);

}
