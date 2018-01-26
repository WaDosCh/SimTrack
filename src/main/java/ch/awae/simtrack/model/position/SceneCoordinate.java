package ch.awae.simtrack.model.position;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class SceneCoordinate {

	public final double s, t;

	public SceneCoordinate(double x, double y) {
		this.s = x;
		this.t = y;
	}

	public double distanceSquared(SceneCoordinate other) {
		return (s * other.s) + (t * other.t);
	}

	public double distance(SceneCoordinate other) {
		return Math.sqrt(distanceSquared(other));
	}

	public String toString() {
		return String.format("SceneCoordinate[s=%1$,.1f, t=%2$,.1f]", this.s,
				this.t);
	}

}
