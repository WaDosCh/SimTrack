package ch.awae.simtrack.model.track;

import javax.json.JsonArray;
import javax.json.JsonObject;

import ch.awae.simtrack.model.BasicTrackTile;
import ch.awae.simtrack.model.ITransformableTile;
import ch.awae.simtrack.model.TilePath;
import ch.awae.simtrack.model.position.Edge;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * mutable track implementation. used by the editor
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
class MutableTrack extends BasicTrackTile implements ITransformableTile {

	private int rotation = 0;
	private TilePath[] links;
	private boolean specialMirror = false;

	MutableTrack(TileCoordinate position, JsonObject json) {
		super(position);
		JsonArray a = json.getJsonArray("links");
		this.links = new TilePath[a.size() / 2];
		for (int i = 0; i + 1 < a.size(); i += 2) {
			this.links[i / 2] = new TilePath(Edge.byIndex(a.getInt(i)), Edge.byIndex(a.getInt(i + 1)));
		}
		this.specialMirror = json.getBoolean("mirror", false);
	}

	private MutableTrack(TileCoordinate position, TilePath[] links, int rotation, boolean specialM) {
		super(position);
		this.links = links;
		this.specialMirror = specialM;
		this.rotation = rotation;
	}

	@Override
	public float getTravelCost() {
		return 1 + (this.links.length * 0.05f);
	}

	@Override
	public TilePath[] getRailPaths() {
		return this.links;
	}

	@Override
	public ITransformableTile rotated(boolean clockwise) {
		TilePath[] links = new TilePath[this.links.length];
		for (int i = 0; i < this.links.length; i++) {
			links[i] = new TilePath(this.links[i]._1.getNeighbour(clockwise), this.links[i]._2.getNeighbour(clockwise));
		}
		return new MutableTrack(this.getTileCoordinate(), links, (rotation + (clockwise ? 1 : 5)) % 6, this.specialMirror);
	}

	@Override
	public ITransformableTile mirrored() {
		// not the most efficient but ok
		if (!this.specialMirror) {
			return rotated(false).rotated(false).rotated(false);
		} else {
			int rot = this.rotation;
			ITransformableTile tile = this;
			for (int i = 0; i < rot; i++)
				tile = tile.rotated(false);
			TilePath[] links = new TilePath[tile.getRailPaths().length];
			for (int i = 0; i < tile.getRailPaths().length; i++)
				links[i] = new TilePath(Edge.byIndex((6 - tile.getRailPaths()[i]._1.ordinal()) % 6),
						Edge.byIndex((6 - tile.getRailPaths()[i]._2.ordinal()) % 6));
			tile = new MutableTrack(this.getTileCoordinate(), links, 0, this.specialMirror);
			for (int i = 0; i < rot; i++)
				tile = tile.rotated(true);
			return tile;
		}
	}

	@Override
	public ITransformableTile cloneTile() {
		return new MutableTrack(this.getTileCoordinate(), this.links.clone(), rotation, this.specialMirror);
	}

	@Override
	public ITransformableTile withPosition(TileCoordinate position) {
		return new MutableTrack(position, links, rotation, specialMirror);
	}

}