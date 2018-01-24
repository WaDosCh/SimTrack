package ch.awae.simtrack.model.position;

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

}
