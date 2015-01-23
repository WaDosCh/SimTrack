package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.TileCoordinate;

public abstract class BasicTrackTile implements ITile {

	private TileCoordinate position;

	public BasicTrackTile(TileCoordinate position) {
		this.position = position;
	}

	public void setPosition(TileCoordinate position) {
		this.position = position;
	}

	public TileCoordinate getPosition() {
		return this.position;
	}

	@Override
	public boolean isFixed() {
		return false;
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
	public boolean connectsAt(int edge) {
		int[] paths = this.getRailPaths();
		for (int e : paths)
			if (e == edge)
				return true;
		return false;
	}

	@Override
	public void tick() {
		return;
	}

	@Override
	public void update(IModel model) {
		return;
	}

	private IBlock block;

	@Override
	public void setBlock(IBlock block) {
		this.block = block;
	}

	@Override
	public IBlock getBlock() {
		return this.block;
	}

}
