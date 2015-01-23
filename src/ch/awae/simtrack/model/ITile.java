/*
 * SimTrack - Railway Planning and Simulation Game
 * Copyright (C) 2015 Andreas Wälchli
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
