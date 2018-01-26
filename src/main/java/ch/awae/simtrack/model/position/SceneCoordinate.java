package ch.awae.simtrack.model.position;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class SceneCoordinate {

	public final double x, y;

	public SceneCoordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distanceSquared(SceneCoordinate other) {
		return (x * other.x) + (y * other.y);
	}

	public double distance(SceneCoordinate other) {
		return Math.sqrt(distanceSquared(other));
	}

	public String toString() {
		return String.format("SceneCoordinate[s=%1$,.1f, t=%2$,.1f]", this.x,
				this.y);
	}

}
