package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.Edge;

public class ObstacleTile implements ITile {

	public ObstacleTile() {
	}

	@Override
	public void tick() {
	}

	@Override
	public void update(IModel model) {
	}

	@Override
	public boolean isFixed() {
		return true;
	}

	@Override
	public boolean isTrainSpawner() {
		return false;
	}

	@Override
	public boolean isTrainDestination() {
		return false;
	}

	@Override
	public float getTravelCost() {
		return 0;
	}

	@Override
	public boolean connectsAt(Edge edge) {
		return false;
	}

	@Override
	public TilePath[] getRailPaths() {
		return null;
	}

	@Override
	public TileType getType() {
		return TileType.OBSTACLE;
	}

}
