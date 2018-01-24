package ch.awae.simtrack.model.track;

import javax.json.JsonArray;
import javax.json.JsonObject;

import ch.awae.simtrack.model.BasicTrackTile;
import ch.awae.simtrack.model.ITransformableTile;
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
	private int[] links;
	private boolean specialMirror = false;

	MutableTrack(TileCoordinate position, JsonObject json) {
		super(position);
		JsonArray a = json.getJsonArray("links");
		this.links = new int[a.size()];
		for (int i = 0; i < a.size(); i++) {
			this.links[i] = a.getInt(i);
		}
		this.specialMirror = json.getBoolean("mirror", false);
	}

	private MutableTrack(TileCoordinate position, int[] links, boolean specialM) {
		super(position);
		this.links = links;
		this.specialMirror = specialM;
	}

	@Override
	public float getTravelCost() {
		return 1 + (this.links.length * 0.05f);
	}

	@Override
	public int[] getRailPaths() {
		return this.links.clone();
	}

	@Override
	public ITransformableTile rotated(boolean clockwise) {
		int[] links = new int[this.links.length];
		for (int i = 0; i < this.links.length; i++) {
			links[i] = (this.links[i] + (clockwise ? 5 : 1)) % 6;
		}
		return new MutableTrack(this.getPosition(), links, this.specialMirror);
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
				tile = tile.rotated(true);
			int[] links = new int[this.links.length];
			for (int i = 0; i < this.links.length; i++)
				links[i] = ((this.links[i] * -1) + 6) % 6;
			tile = new MutableTrack(this.getPosition(), links, this.specialMirror);
			for (int i = 0; i < rot; i++)
				tile = tile.rotated(false);
			return tile;
		}
	}

	@Override
	public ITransformableTile cloneTile() {
		return new MutableTrack(this.getPosition(), this.links.clone(), this.specialMirror);
	}

	@Override
	public ITransformableTile withPosition(TileCoordinate position) {
		return new MutableTrack(position, links, specialMirror);
	}

}
