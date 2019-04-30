package ch.awae.simtrack.scene.game.model.position;

import ch.judos.generic.data.geometry.PointD;

public enum Edge {

	RIGHT(1, 0), //
	BOTTOM_RIGHT(0, 1), //
	BOTTOM_LEFT(-1, 1), //
	LEFT(-1, 0), //
	TOP_LEFT(0, -1), //
	TOP_RIGHT(1, -1);

	public final int Δu, Δv;

	Edge(int Δu, int Δv) {
		this.Δu = Δu;
		this.Δv = Δv;
	}

	// XXX: rename to "byOrdinal"
	public static Edge byIndex(int index) {
		return values()[index];
	}

	public Edge getOpposite() {
		return byIndex((ordinal() + 3) % 6);
	}

	public Edge getNeighbour(boolean clockwise) {
		return byIndex((ordinal() + (clockwise ? 1 : 5)) % 6);
	}
	
	public boolean isNeighbour(Edge other) {
		return getNeighbour(false) == other || getNeighbour(true) == other;
	}

	public Edge getNeighbourX(int clockwiseSteps) {
		// make sure negative values work:
		clockwiseSteps = (clockwiseSteps % 6) + 6;
		return byIndex((ordinal() + clockwiseSteps) % 6);
	}

	/**
	 * @return direction from the edge away from the tile center
	 */
	public double getAngleOut() {
		return this.ordinal() * 1. / 6. * 2. * Math.PI;
	}

	/**
	 * @return direction from the edge to the center of the tile
	 */
	public double getAngleIn() {
		return getAngleOut() + Math.PI;
	}

	/**
	 * @return the position offset from the center of a tile
	 */
	public PointD getPosition() {
		PointD position = new PointD(50, 0);
		position.rotate(getAngleOut());
		return position;
	}

	public Edge mirrorAlong(Edge axis) {
		int delta = ordinal() - axis.ordinal();
		int newOrd = (axis.ordinal() - delta + 6 ) % 6;
		return values()[newOrd];
	}

}
