package ch.awae.simtrack.scene.game.model;

import ch.awae.simtrack.scene.game.model.entity.Signal;
import ch.awae.simtrack.scene.game.model.entity.Signal.Type;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.track.BorderTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.ConstructionTrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackTile;
import ch.awae.simtrack.scene.game.model.tile.track.TrackValidator;

/**
 * an object to define at runtime which actions on the model are allowed and which are not
 */
public class ModelRules {

	private Model model;

	public ModelRules(Model model) {
		this.model = model;
	}

	public boolean canPlaceSignal(TileEdgeCoordinate position, Type type) {
		if (this.model.getSignals().containsKey(position))
			return false;
		// check if signal position is valid
		Tile tile = this.model.getTileAt(position.tile);
		if (tile == null || (tile instanceof BorderTrackTile && ((BorderTrackTile) tile).isTrainDestination())
				|| !(tile instanceof TrackTile))
			return false;
		Signal opponent = this.model.getSignalAt(position.getOppositeDirection());
		if (opponent != null) {
			if (opponent.getType() == Type.ONE_WAY || type == Type.ONE_WAY)
				return false;
		}
		TrackTile track = (TrackTile) tile;
		return track.connectsAt(position.edge);
	}

	public boolean canRemoveSignalAt(TileEdgeCoordinate position) {
		Tile raw_tile = this.model.getTileAt(position.tile);
		if (!(raw_tile instanceof TrackTile))
			return false;
		TrackTile tile = (TrackTile) raw_tile;
		Signal current = this.model.getSignalAt(position);
		return !(current == null || tile instanceof BorderTrackTile);
	}

	/**
	 * checks if the tile at a given location in a given model can be deleted
	 * 
	 * @param c the location to check on
	 * @param m the model to check in
	 * @return {@code true} if and only if the given position in the given model contains a tile and it can be deleted.
	 */
	public boolean canBulldoze(TileCoordinate tile) {
		if (tile == null)
			return false;
		Tile t = this.model.getTileAt(tile);
		if (t == null || t instanceof FixedTile)
			return false;
		if (this.model.playerMoney < this.model.getBulldozeCost())
			return false;
		return true;
	}

	public boolean canPlaceTrack(TileCoordinate tileCoord, ConstructionTrackTile track) {
		if (tileCoord == null)
			return false;
		if (!this.model.isOnMap(tileCoord))
			return false;

		Tile tile = model.getTileAt(tileCoord);
		if (track.getBuildCost() > this.model.playerMoney)
			return false;

		if (tile == null)
			return true;
		if (tile instanceof FixedTile)
			return false;
		if (tile instanceof TrackTile) {
			TrackTile ttile = (TrackTile) tile;
			ConstructionTrackTile fusedTrack = track.fuseWith(ttile);
			return TrackValidator.isValidTrack(fusedTrack) && !fusedTrack.getNormalTrackTile().equals(ttile)
					&& fusedTrack.getBuildCost() <= this.model.playerMoney;
		}
		return false;
	}

}
