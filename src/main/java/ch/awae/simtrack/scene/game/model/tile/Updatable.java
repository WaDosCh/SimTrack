package ch.awae.simtrack.scene.game.model.tile;

public interface Updatable {
	/**
	 * do an update cycle
	 */
	public default void update() {
	}

	/**
	 * @return how many ticks till the Tile is updated again<br>
	 *         <b>NOTE</b>: 0 means do not tick this Tile anymore.
	 */
	public default long getUpdateEveryTicks() {
		return 0;
	}
}
