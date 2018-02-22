package ch.awae.simtrack.scene.game.model.position;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class TilePathCoordinate implements Serializable {
	
	private static final long serialVersionUID = -7444282501038833659L;
	
	private @Getter TileCoordinate tile;
	private @Getter TilePath path;

	/**
	 * @param progressedDistance
	 * @return
	 */
	public SceneCoordinate getPositionOnPath(double progressedDistance) {
		SceneCoordinate tilePos = this.tile.toSceneCoordinate();
		SceneCoordinate delta = this.path.getPosition(progressedDistance);
		return new SceneCoordinate(tilePos.s + delta.s, tilePos.t + delta.t);
	}

	public double getPathLength() {
		return this.path.getPathLength();
	}

}
