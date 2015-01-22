package ch.awae.simtrack.controller;

import java.awt.Point;
import java.util.ArrayList;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.CurvedRail;
import ch.awae.simtrack.model.track.StraightCrossing;
import ch.awae.simtrack.model.track.StraightRail;
import ch.awae.simtrack.view.ARenderer;
import ch.awae.simtrack.view.renderer.TrackBarRenderer;

public class TrackBar implements ITool {

	private ARenderer rend;
	private ArrayList<TrackTile> tracks;
	private int index;

	public TrackBar() {
		this.rend = new TrackBarRenderer(this);
		this.init();
	}

	private void init() {
		this.tracks = new ArrayList<>();
		this.tracks.add(new StraightRail(new TileCoordinate(0, 0)));
		this.tracks.add(new CurvedRail(new TileCoordinate(0, 0)));
		this.tracks.add(new StraightCrossing(new TileCoordinate(0, 0)));
	}

	@Override
	public void load() throws IllegalStateException {

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	public ArrayList<TrackTile> getTracks() {
		return tracks;
	}

	public int getIndex() {
		return index;
	}
	@Override
	public void tick() {
		this.index = -1;
		Point p = Global.mouseObserver.mouse;
		if (p == null) {
			p = new Point(0, 0);
		}
		p = p.getLocation();
		p.x -= Global.ScreenW / 2;
		p.x += 550;
		p.y -= Global.ScreenH;
		p.y += 100;
		if (p.x < 0 || p.y < 0)
			return;
		int index = p.x / 100;
		if (index > 9)
			return;
		this.index = index;
		// index holds the track in the menu
	}

	@Override
	public String getToolName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ARenderer getRenderer() {
		return this.rend;
	}

}