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
import java.awt.event.KeyEvent;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.EventDrivenTool;
import ch.awae.simtrack.controller.input.Input;
import ch.awae.simtrack.model.track.TrackProvider;
import ch.awae.simtrack.view.IRenderer;
import lombok.Getter;

/**
 * Track tool-bar used for track selection while editing the board
 * 
 * @author Andreas Wälchli
 * @version 1.5, 2015-01-26
 * @since SimTrack 0.2.2 (0.2.1)
 */
public class TrackBar extends EventDrivenTool {

	private int index;
	private @Getter IRenderer renderer = new TrackBarRenderer(this);
	
	
	/**
	 * creates a new track-bar instance
	 * 
	 * @param editor
	 *            the editor owning the build tool
	 */
	public TrackBar(Editor editor) {
	    super(editor, UnloadAction.IGNORE);
	    
	    onPress(KeyEvent.VK_MINUS, () -> select(0));
        onPress(KeyEvent.VK_1, () -> select(1));
        onPress(KeyEvent.VK_2, () -> select(2));
        onPress(KeyEvent.VK_3, () -> select(3));
        onPress(KeyEvent.VK_4, () -> select(4));
        onPress(KeyEvent.VK_5, () -> select(5));
        onPress(KeyEvent.VK_6, () -> select(6));
        onPress(KeyEvent.VK_7, () -> select(7));
        onPress(KeyEvent.VK_8, () -> select(8));
        onPress(KeyEvent.VK_9, () -> select(9));
        onPress(KeyEvent.VK_0, () -> select(10));
        
        onTick(this::updateByMouse);
        
        onPress(Input.MOUSE_LEFT, this::select);
	}

	int getIndex() {
		return this.index;
	}

	private void select(int index) {
	    this.index = index;
	    select();
	}
	
	private void select() {
		if (this.index == 0) {
			this.editor.loadTool(BuildTool.class);
		} else {
			if (this.index <= TrackProvider.getTileCount()) {
				// TrackTile t = this.tracks.get(this.index - 1).cloneTrack();
				this.editor.loadTool(BuildTool.class, new Object[] {
						TrackProvider.getTileInstance(this.index - 1) });
			}
		}
	}

	public void updateByMouse() {
		this.index = -1;
		Point p = mousePosition;
		if (p == null) {
			p = new Point(0, 0);
		}
		p = p.getLocation();
		p.x -= controller.getGameView().getHorizontalScreenSize() / 2;
		p.x += 550;
		p.y -= controller.getGameView().getVerticalScreenSize();
		p.y += 100;
		if (p.x < 0 || p.y < 0)
			return;
		int index = p.x / 100;
		if (index > 10)
			return;
		this.index = index;
	}

}
