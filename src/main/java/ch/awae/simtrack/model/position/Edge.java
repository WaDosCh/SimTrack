package ch.awae.simtrack.model.position;

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

	public static Edge byIndex(int index) {
		return values()[index];
	}

	public Edge getOpposite() {
		return byIndex((ordinal() + 3) % 6);
	}

	public Edge getNeighbour(boolean clockwise) {
		return byIndex((ordinal() + (clockwise ? 1 : 5)) % 6);
	}

}
