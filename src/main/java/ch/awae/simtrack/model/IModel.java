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

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.util.T3;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IModel {

	public int getHorizontalSize();

	public int getVerticalSize();

	public ITile getTileAt(TileCoordinate position);

	public void setTileAt(TileCoordinate position, ITile tile);

	public Set<Map.Entry<TileCoordinate, ITile>> getTiles();

	public void removeTileAt(TileCoordinate position) throws IllegalArgumentException;

	default void update() {}

	default void tick() {}

	public List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> getPaths(TileCoordinate position);

	public Set<Map.Entry<TileEdgeCoordinate, Signal>> getSignals();
	
	Signal getSignalAt(TileEdgeCoordinate position);
	
	void setSignalAt(TileEdgeCoordinate position, Signal signal);

}
