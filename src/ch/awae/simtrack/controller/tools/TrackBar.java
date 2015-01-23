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
package ch.awae.simtrack.controller.tools;

import java.awt.Point;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.model.track.TrackProvider;
import ch.awae.simtrack.view.IRenderer;

/**
 * Track tool-bar used for track selection while editing the board
 * 
 * @author Andreas Wälchli
 * @version 1.3, 2015-01-23
 * @since SimTrack 0.2.1
 */
public class TrackBar {

	private int index;
	private boolean isPressed = false;
	private IRenderer rend;
	private Editor editor;

	public TrackBar(Editor editor) {
		this.editor = editor;
		this.rend = new TrackBarRenderer(this);
	}

	public int getIndex() {
		return this.index;
	}

	public IRenderer getRenderer() {
		return this.rend;
	}

	private void select() {
		if (this.index == 0) {
			this.editor.loadTool("Builder", null);
		} else {
			if (this.index <= TrackProvider.getTileCount()) {
				// TrackTile t = this.tracks.get(this.index - 1).cloneTrack();
				this.editor.loadTool("Builder", new Object[] { TrackProvider
						.getTileInstance(this.index - 1) });
			}
		}
	}

	public void tick() {
		this.index = -1;
		Point p = this.editor.getController().getMouse().position();
		if (p == null) {
			p = new Point(0, 0);
		}
		p = p.getLocation();
		p.x -= this.editor.getController().getView().getHorizontalScreenSize() / 2;
		p.x += 550;
		p.y -= this.editor.getController().getView().getVerticalScreenSize();
		p.y += 100;
		if (p.x < 0 || p.y < 0)
			return;
		int index = p.x / 100;
		if (index > 10)
			return;
		this.index = index;
		// index holds the track in the menu
		boolean button = this.editor.getController().getMouse().button1();
		if (button && !this.isPressed) {
			this.isPressed = true;
			this.select();
		} else if (!button && this.isPressed) {
			this.isPressed = false;
		}
	}

}
