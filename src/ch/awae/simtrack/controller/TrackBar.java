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

import java.awt.Point;
import java.util.ArrayList;

import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.gui.Surface;
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
import ch.awae.simtrack.view.IRenderer;
import ch.awae.simtrack.view.renderer.TrackBarRenderer;

/**
 * Track tool-bar used for track selection while editing the board
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class TrackBar {

	private int index;
	private boolean isPressed = false;
	private IRenderer rend;

	private ArrayList<TrackTile> tracks;

	public TrackBar() {
		this.rend = new TrackBarRenderer(this);
		this.init();
	}

	public int getIndex() {
		return this.index;
	}

	public IRenderer getRenderer() {
		return this.rend;
	}

	public ArrayList<TrackTile> getTracks() {
		return this.tracks;
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

	private void select() {
		if (this.index == 0) {
			Editor.loadTool("Builder", null);
		} else {
			if (this.index <= this.tracks.size()) {
				TrackTile t = this.tracks.get(this.index - 1).cloneTrack();
				Editor.loadTool("Builder", new Object[] { t });
			}
		}
	}

	public void tick() {
		this.index = -1;
		Point p = Mouse.position();
		if (p == null) {
			p = new Point(0, 0);
		}
		p = p.getLocation();
		p.x -= Surface.instance().getWidth() / 2;
		p.x += 550;
		p.y -= Surface.instance().getHeight();
		p.y += 100;
		if (p.x < 0 || p.y < 0)
			return;
		int index = p.x / 100;
		if (index > 10)
			return;
		this.index = index;
		// index holds the track in the menu
		boolean button = Mouse.button1();
		if (button && !this.isPressed) {
			this.isPressed = true;
			this.select();
		} else if (!button && this.isPressed) {
			this.isPressed = false;
		}
	}

}
