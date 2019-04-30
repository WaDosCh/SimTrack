package ch.awae.simtrack.scene.game.model.tile.track;

import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.scene.game.model.tile.FixedTile;
import lombok.Getter;

/**
 * Implementation for the border track pieces. They do not contain any paths.
 */
public class BorderTrackTile extends TrackTile implements FixedTile {

	private static final long serialVersionUID = -2434920987381887298L;
	private final boolean isOutput;
	private @Getter final Edge startingEdge;

	public BorderTrackTile(Edge edge, boolean isOutput) {
		super(new TilePath(edge, edge.getOpposite()));
		this.isOutput = isOutput;
		this.startingEdge = edge;
	}

	public boolean isTrainSpawner() {
		return !isOutput;
	}

	public boolean isTrainDestination() {
		return !isTrainSpawner();
	}

}
