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
package ch.awae.simtrack.controller;

import static ch.awae.simtrack.HighLogic.map;

import java.util.ArrayList;

import ch.awae.simtrack.model.Block;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.position.TileEdgeCoordinate;

/**
 * Block Builder. Constructs block structure from raw map
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.1.1
 */
public class BlockBuilder {

	public static void updateBlocks() {
		// give a block each
		map.getBorderTracks().forEach((p, t) -> createBlockFor(t));
		map.getTrackPieces().forEach((p, t) -> createBlockFor(t));
		// merge blocks where possible
		map.getBorderTracks().forEach((p, t) -> mergeIfPossible(t));
		map.getTrackPieces().forEach((p, t) -> mergeIfPossible(t));
		// extract blocks
		ArrayList<Block> blocks = new ArrayList<>();
		map.getBorderTracks().forEach((p, t) -> addBlockIfNew(t, blocks));
		map.getTrackPieces().forEach((p, t) -> addBlockIfNew(t, blocks));
		// list now holds all blocks
		map.blocks = blocks;
	}

	private static void createBlockFor(TrackTile tile) {
		int u = tile.getPosition().getU();
		int v = tile.getPosition().getV();
		int id = ((u + v) * (u + v)) + v;
		tile.setBlock(new Block(id));
	}

	private static void mergeIfPossible(TrackTile tile) {
		int u = tile.getPosition().getU();
		int v = tile.getPosition().getV();
		float[][] paths = tile.getRawPaths();
		for (int i = 0; i < 2 * paths.length; i++) {
			int edge = (int) paths[i % paths.length][i < paths.length ? 0 : 1];

			int du = ((edge % 3) < 2 ? 1 : 0) * (edge < 3 ? 1 : -1);
			int dv = ((edge % 3) > 0 ? 1 : 0) * (edge > 2 ? 1 : -1);

			TrackTile neighbour = map.getTileAt(new TileCoordinate(u + du, v
					+ dv));
			if (neighbour == null)
				continue;
			if (map.getSignalAt(new TileEdgeCoordinate(u, v, edge)) != null)
				continue;
			if (neighbour.connectsToEdge((edge + 3) % 6))
				tile.getBlock().merge(neighbour.getBlock());
		}
	}

	private static void addBlockIfNew(TrackTile tile, ArrayList<Block> list) {
		Block b = tile.getBlock();
		if (!list.contains(b))
			list.add(b);
	}
}
