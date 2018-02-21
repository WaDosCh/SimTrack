package ch.awae.simtrack.scene.game.model.position;

import ch.judos.generic.data.geometry.PointD;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class SceneCoordinate {

	public final double s, t;

	public SceneCoordinate(double s, double t) {
		this.s = s;
		this.t = t;
	}

	public double distanceSquared(SceneCoordinate other) {
		return (s * other.s) + (t * other.t);
	}

	public double distance(SceneCoordinate other) {
		return Math.sqrt(distanceSquared(other));
	}

	public String toString() {
		return String.format("SceneCoordinate[s=%1$,.1f, t=%2$,.1f]", this.s, this.t);
	}

	public TileCoordinate toTileCoordinate() {
		double v = (2.0 * t) / (Math.sqrt(3) * 100);
		double u = (1.0 * s) / 100 - v / 2;
		int baseU = (int) Math.floor(u);
		int baseV = (int) Math.floor(v);
		u -= baseU;
		v -= baseV;

		if ((v < 1 - (2 * u)) && (v < (1 - u) / 2)) {
			// aok
		} else if ((v > 2 - (2 * u)) && (v > 1 - (u / 2))) {
			baseU++;
			baseV++;
		} else if (v < u) {
			baseU++;
		} else {
			baseV++;
		}

		return new TileCoordinate(baseU, baseV);
	}

	public PointD getPointD() {
		return new PointD(this.s, this.t);
	}

}
