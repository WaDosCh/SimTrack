package ch.awae.simtrack.scene.game.model.entity;

import java.io.Serializable;
import java.util.function.Consumer;

import ch.awae.simtrack.scene.game.model.PathFindingRequest;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface Entity extends Serializable {

	default void tick(Consumer<PathFindingRequest> pathFinding) {
	};

}
