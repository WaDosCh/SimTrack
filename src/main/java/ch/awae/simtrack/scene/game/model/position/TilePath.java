package ch.awae.simtrack.scene.game.model.position;

import java.io.Serializable;

import ch.judos.generic.data.geometry.PointD;

public final class TilePath implements Comparable<TilePath>, Serializable {

	private static final long serialVersionUID = 9173421190168686602L;

	public final Edge edge1, edge2;

	public TilePath(Edge edge1, Edge edge2) {
		if (edge1.isNeighbour(edge2))
			throw new RuntimeException("Invalid tilePath from " + edge1 + " to " + edge2);
		this.edge1 = edge1;
		this.edge2 = edge2;
	}

	public double getPathLength() {
		if (edge1.getOpposite() == edge2)
			return 100;

		// radius of the circle to draw a curved rail
		final double radius = 100 * Math.sqrt(3) / 2;
		final double perimeter = radius * 2 * Math.PI;
		// a curve is basically 60° of the whole perimeter
		return perimeter * 60 / 360;
	}

	public SceneCoordinate getPosition(double progressedDistance) {
		// start of movement
		PointD start = new PointD(-50, 0);
		// build a movement vector when we would go from left to right
		PointD delta;
		if (isStraight()) {
			delta = new PointD(progressedDistance, 0.);
		} else { // curved, assume it's a right curve
			final double radius = 100 * Math.sqrt(3) / 2;
			double deltaAngle = progressedDistance / getPathLength() * 60 / 360 * Math.PI * 2;

			delta = new PointD(0, -radius);
			delta.rotate(deltaAngle); // we turn clockwise
			delta.addI(new PointD(0, radius));
			if (isLeftCurve())
				delta.y *= -1;
		}
		// position
		PointD point = start.add(delta);
		// consider actual direction of path and rotate start,delta vectors
		point.rotate(edge1.getAngleIn());
		return new SceneCoordinate(point.x, point.y);
	}

	public boolean isLeftCurve() {
		return edge1.getNeighbourX(2) == edge2;
	}

	public boolean isStraight() {
		return edge1.getOpposite() == edge2;
	}

	public boolean connectsAt(Edge edge) {
		return this.edge1 == edge || this.edge2 == edge;
	}

	public boolean connectsFromTo(Edge other1, Edge other2) {
		return (this.edge1 == other1 && this.edge2 == other2) || (this.edge2 == other1 && this.edge1 == other1);
	}

	public TilePath mirroredAlong(Edge axis) {
		return new TilePath(edge1.mirrorAlong(axis), edge2.mirrorAlong(axis));
	}

	@Override
	public int hashCode() {
		// make sure that the hash method is commutable i.e.:
		// hashCode(x,y) == hashCode(y,x)
		return edge1.hashCode() + edge2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TilePath other = (TilePath) obj;
		return (edge1 == other.edge1 && edge2 == other.edge2) || (edge2 == other.edge1 && edge1 == other.edge2);
	}

	@Override
	public int compareTo(TilePath o) {
		return Integer.compare(hashCode(), o.hashCode());
	}

	@Override
	public String toString() {
		return "TilePath(" + edge1 + ", " + edge2 + ")";
	}

	public TilePath reversed() {
		return new TilePath(this.edge2, this.edge1);
	}
}
