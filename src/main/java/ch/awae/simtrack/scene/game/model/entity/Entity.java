package ch.awae.simtrack.scene.game.model.entity;

import java.io.Serializable;
import java.util.Set;

import ch.awae.simtrack.core.BaseTicker;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;

/**
 * any moving object on the map is an entity
 */
public interface Entity extends Serializable, BaseTicker {

	Set<TileCoordinate> getReservedTiles();

}
