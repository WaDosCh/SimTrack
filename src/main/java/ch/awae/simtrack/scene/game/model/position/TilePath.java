package ch.awae.simtrack.scene.game.model.position;

import java.io.Serializable;

import ch.judos.generic.data.geometry.PointD;

public final class TilePath implements Comparable<TilePath>, Serializable {

	private static final long serialVersionUID = 9173421190168686602L;

	// XXX: rename to edge1, edge2
	public final Edge _1, _2;

	public TilePath(Edge edge1, Edge edge2) {
		if (edge1.isNeighbour(edge2))
			throw new RuntimeException("Invalid tilePath from " + edge1 + " to " + edge2);
		this._1 = edge1;
		this._2 = edge2;
	}

	public double getPathLength() {
		if (_1.getOpposite() == _2)
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
		point.rotate(_1.getAngleIn());
		return new SceneCoordinate(point.x, point.y);
	}

	public boolean isLeftCurve() {
		return _1.getNeighbourX(2) == _2;
	}

	public boolean isStraight() {
		return _1.getOpposite() == _2;
	}

	public boolean connectsAt(Edge edge) {
		return _1 == edge || _2 == edge;
	}

	public boolean connectsFromTo(Edge edge, Edge edge2) {
		return (_1 == edge && _2 == edge2) || (_2 == edge && _1 == edge);
	}

	public TilePath mirroredAlong(Edge axis) {
		return new TilePath(_1.mirrorAlong(axis), _2.mirrorAlong(axis));
	}

	@Override
	public int hashCode() {
		// make sure that the hash method is commutable i.e.:
		// hashCode(x,y) == hashCode(y,x)
		return _1.hashCode() + _2.hashCode();
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
		return (_1 == other._1 && _2 == other._2) || (_2 == other._1 && _1 == other._2);
	}

	@Override
	public int compareTo(TilePath o) {
		return Integer.compare(hashCode(), o.hashCode());
	}

	@Override
	public String toString() {
		return "TilePath(" + _1 + ", " + _2 + ")";
	}
}
