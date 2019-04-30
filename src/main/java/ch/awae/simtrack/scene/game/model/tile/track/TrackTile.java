package ch.awae.simtrack.scene.game.model.tile.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import ch.awae.simtrack.scene.game.model.position.Edge;
import ch.awae.simtrack.scene.game.model.position.TileCoordinate;
import ch.awae.simtrack.scene.game.model.position.TileEdgeCoordinate;
import ch.awae.simtrack.scene.game.model.position.TilePath;
import ch.awae.simtrack.scene.game.model.tile.Tile;
import ch.awae.simtrack.scene.game.model.tile.TileType;
import ch.awae.utils.functional.T3;
import lombok.Getter;

public class TrackTile implements Tile {

	private static final long serialVersionUID = -907886762191687187L;

	/**
	 * sorted in constructor TrackTile, make sure not to modify them for hashCode + equals to work<br>
	 * Stored paths are undirected. a way is stored only once and not backwards as well. e.g. only connection 0,3 is
	 * stored and not also 3,0.
	 * 
	 */
	protected final TilePath[] paths;

	public TrackTile(TilePath... paths) {
		Arrays.sort(paths);
		this.paths = paths;
	}

	@Override
	public TileType getType() {
		return TileType.TRACK;
	}

	public boolean connectsAt(Edge edge) {
		for (TilePath p : this.paths)
			if (p.connectsAt(edge))
				return true;
		return false;
	}

	public boolean connectsFromTo(Edge edge, Edge edge2) {
		for (TilePath p : this.paths) {
			if (p.connectsFromTo(edge, edge2))
				return true;
		}
		return false;
	}

	public TrackTile fuseWith(TrackTile tile) {
		HashSet<TilePath> paths = new HashSet<>();
		for (TilePath path : this.paths)
			paths.add(path);
		for (TilePath path : tile.paths) {
			if (!paths.contains(path))
				paths.add(path);
		}
		return new TrackTile(paths.toArray(new TilePath[0]));
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(paths);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackTile other = (TrackTile) obj;
		if (!Arrays.equals(paths, other.paths))
			return false;
		return true;
	}

	// XXX: change return type to use TilePathCoordinate (also includes path length, so T3 not needed)
	public List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> getAllDirectedPaths(TileCoordinate position) {
		List<T3<TileEdgeCoordinate, TileEdgeCoordinate, Float>> result = new ArrayList<>();
		for (TilePath p : this.paths) {
			TileEdgeCoordinate from = new TileEdgeCoordinate(position, p._1);
			TileEdgeCoordinate to = new TileEdgeCoordinate(position, p._2);
			float cost = (float) p.getPathLength();
			result.add(new T3<>(from.getOppositeDirection(), to, cost));
			result.add(new T3<>(to.getOppositeDirection(), from, cost));
		}
		return result;
	}
	
	/**
	 * @return all undirected paths, if you want a path for both directions use getAllDirectedPaths
	 */
	public TilePath[] getPaths() {
		return this.paths;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("TrackTile(");
		for (TilePath p : this.paths) {
			str.append(p._1 + ", " + p._2 + " / ");
		}
		String st = str.toString();
		return st.substring(0, st.length() - 3) + ")";
	}

}
