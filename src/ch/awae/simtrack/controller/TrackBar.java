package ch.awae.simtrack.controller;

import java.awt.Point;
import java.util.ArrayList;

import ch.awae.simtrack.Global;
import ch.awae.simtrack.model.TrackTile;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.model.track.BasicTurnout;
import ch.awae.simtrack.model.track.CurvedCrossing;
import ch.awae.simtrack.model.track.CurvedRail;
import ch.awae.simtrack.model.track.DoubleSlip;
import ch.awae.simtrack.model.track.SingleSlip;
import ch.awae.simtrack.model.track.StraightCrossing;
import ch.awae.simtrack.model.track.StraightCurvedCrossing;
import ch.awae.simtrack.model.track.StraightRail;
import ch.awae.simtrack.model.track.ThreeWayTurnout;
import ch.awae.simtrack.model.track.WyeSwitch;
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
		this.tracks.add(new StraightCurvedCrossing(new TileCoordinate(0, 0)));
		this.tracks.add(new CurvedCrossing(new TileCoordinate(0, 0)));
		this.tracks.add(new BasicTurnout(new TileCoordinate(0, 0)));
		this.tracks.add(new ThreeWayTurnout(new TileCoordinate(0, 0)));
		this.tracks.add(new WyeSwitch(new TileCoordinate(0, 0)));
		this.tracks.add(new SingleSlip(new TileCoordinate(0, 0)));
		this.tracks.add(new DoubleSlip(new TileCoordinate(0, 0)));
	}

	@Override
	public void load(Object[] args) throws IllegalStateException {
		// no action required
	}

	@Override
	public void unload() {
		// no action required
	}

	public ArrayList<TrackTile> getTracks() {
		return this.tracks;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public void tick() {
		this.index = -1;
		Point p = Global.mouse.position();
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
		if (index > 10)
			return;
		this.index = index;
		// index holds the track in the menu
		boolean button = Global.mouse.button1();
		if (button && !this.isPressed) {
			this.isPressed = true;
			this.select();
		} else if (!button && this.isPressed) {
			this.isPressed = false;
		}
	}

	private void select() {
		if (this.index == 0) {
			Global.editor.loadTool("Builder", null);
		} else {
			if (this.index <= this.tracks.size()) {
				TrackTile t = this.tracks.get(this.index - 1).cloneTrack();
				Global.editor.loadTool("Builder", new Object[] { t });
			}
		}
	}

	private boolean isPressed = false;

	@Override
	public String getToolName() {
		return null;
	}

	@Override
	public ARenderer getRenderer() {
		return this.rend;
	}

}
