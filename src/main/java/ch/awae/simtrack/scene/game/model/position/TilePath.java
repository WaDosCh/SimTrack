package ch.awae.simtrack.scene.game.model.position;

import java.io.Serializable;

import ch.judos.generic.data.geometry.PointD;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class TilePath implements Comparable<TilePath>, Serializable {

	private static final long serialVersionUID = 9173421190168686602L;
	public final Edge _1, _2;

	public TilePath(Edge _1, Edge _2) {
		if (_1.getNeighbour(false) == _2 || _2.getNeighbour(true) == _2)
			throw new RuntimeException("Invalid tilePath from " + _1 + " to " + _2);
		this._1 = _1;
		this._2 = _2;
	}

	public TilePath normalised() {
		if (_1.ordinal() > _2.ordinal())
			return new TilePath(_2, _1);
		else
			return this;
	}

	@Override
	public int compareTo(TilePath path) {
		int high = _1.compareTo(path._1);
		if (high == 0)
			return _2.compareTo(path._2);
		else
			return high;
	}

	public TilePath swap() {
		return new TilePath(_2, _1);
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
		if (_1.getOpposite() == _2) { // straight
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

}
