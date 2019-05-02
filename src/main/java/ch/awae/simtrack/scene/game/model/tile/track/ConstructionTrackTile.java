package ch.awae.simtrack.scene.game.model.tile.track;

import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import lombok.Getter;

public class ConstructionTrackTile extends TrackTile {

	private static final long serialVersionUID = -2848276508620233569L;

	protected Edge baseEdge; // use for the mirror function
	protected @Getter int buildCost;
	protected @Getter boolean showInToolbar;

	public ConstructionTrackTile(TilePath[] paths, Edge baseEdge, int buildCost, boolean showInToolbar) {
		super(paths);
		this.baseEdge = baseEdge;
		this.buildCost = buildCost;
		this.showInToolbar = showInToolbar;
	}

	/**
	 * @param clockwise
	 * @return returns a new Track tile which is rotated
	 */
	public ConstructionTrackTile rotated(boolean clockwise) {
		TilePath[] links = new TilePath[this.paths.length];
		for (int i = 0; i < this.paths.length; i++) {
			links[i] = new TilePath(this.paths[i].edge1.getNeighbour(clockwise), this.paths[i].edge2.getNeighbour(clockwise));
		}
		return new ConstructionTrackTile(links, this.baseEdge.getNeighbour(clockwise), this.buildCost, this.showInToolbar);
	}

	/**
	 * @return a new Track tile which is mirrored along the baseEdge
	 */
	public ConstructionTrackTile mirrored() {
		TilePath[] paths = new TilePath[this.paths.length];
		for (int i = 0; i < this.paths.length; i++) {
			paths[i] = this.paths[i].mirroredAlong(this.baseEdge);
		}
		return new ConstructionTrackTile(paths, this.baseEdge, this.buildCost, this.showInToolbar);
	}
	
	public TrackTile getNormalTrackTile() {
		return new TrackTile(this.paths);
	}
}
