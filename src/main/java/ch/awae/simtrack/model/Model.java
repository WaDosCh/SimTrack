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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.awae.simtrack.model.position.TileCoordinate;

class Model implements IModel {

	private int sizeX, sizeY;

	Model(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	private HashMap<TileCoordinate, ITile> tiles = new HashMap<>();

	@Override
	public int getHorizontalSize() {
		return this.sizeX;
	}

	@Override
	public int getVerticalSize() {
		return this.sizeY;
	}

	@Override
	public ITile getTileAt(TileCoordinate position) {
		return this.tiles.get(position);
	}

	@Override
	public void setTileAt(TileCoordinate position, ITile tile) {
		if (this.tiles.containsKey(position))
			return;
		this.tiles.put(position, tile);
	}

	@Override
	public Set<Map.Entry<TileCoordinate, ITile>> getTiles() {
		return tiles.entrySet();
	}

	@Override
	public void removeTileAt(TileCoordinate position) throws IllegalArgumentException {
		ITile tile = this.tiles.get(position);
		if (tile == null || tile.isFixed())
			throw new IllegalArgumentException();
		this.tiles.remove(position);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

}
