package ch.awae.simtrack.model;

import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface ITile extends IEntity {

	public TileCoordinate getPosition();

	public void setPosition(TileCoordinate position);

	public boolean isFixed();

	public boolean isTrainSpawner();

	public boolean isTrainDestination();

	public float getTravelCost();

	public int[] getRailPaths();

	public void rotate(boolean clockwise);

	public void mirror();

	public boolean connectsAt(int edge);

	public ITile cloneTile();
	
	public void setBlock(IBlock block);
	
	public IBlock getBlock();

}
