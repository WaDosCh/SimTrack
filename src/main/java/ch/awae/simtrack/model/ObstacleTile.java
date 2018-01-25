package ch.awae.simtrack.model;

public class ObstacleTile implements IFixedTile {

	public final static ObstacleTile INSTANCE = new ObstacleTile();

	private ObstacleTile() {
		// empty constructor
	};

	@Override
	public TileType getType() {
		return TileType.OBSTACLE;
	}

}
