package ch.awae.simtrack.model.track;

import javax.json.JsonArray;
import javax.json.JsonObject;

import ch.awae.simtrack.model.BasicTrackTile;
import ch.awae.simtrack.model.ITile;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * mutable track implementation. used by the editor
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
class MutableTrack extends BasicTrackTile {

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
	public void rotate(boolean clockwise) {
		this.rotation += clockwise ? 5 : 1;
		this.rotation %= 6;
		for (int i = 0; i < this.links.length; i++) {
			this.links[i] = (this.links[i] + (clockwise ? 5 : 1)) % 6;
		}
	}

	@Override
	public void mirror() {
		if (!this.specialMirror) {
			this.rotate(false);
			this.rotate(false);
			this.rotate(false);
		} else {
			int rot = this.rotation;
			for (int i = 0; i < rot; i++)
				this.rotate(true);
			for (int i = 0; i < this.links.length; i++)
				this.links[i] = ((this.links[i] * -1) + 6) % 6;
			for (int i = 0; i < rot; i++)
				this.rotate(false);
		}
	}

	@Override
	public ITile cloneTile() {
		return new MutableTrack(this.getPosition(), this.links.clone(),
				this.specialMirror);
	}

}
