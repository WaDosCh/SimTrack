package ch.awae.simtrack.model;

import java.io.Serializable;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IEntity extends Serializable {

	default void tick() {
	};

	default void update(IModel model) {
	};

}
