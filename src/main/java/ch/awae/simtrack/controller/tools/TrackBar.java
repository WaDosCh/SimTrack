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
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.model.track.TrackProvider;
import ch.awae.simtrack.view.IRenderer;

/**
 * Track tool-bar used for track selection while editing the board
 * 
 * @author Andreas Wälchli
 * @version 1.5, 2015-01-26
 * @since SimTrack 0.2.2 (0.2.1)
 */
public class TrackBar {

	private int index;
	private boolean isPressed = false;
	private IRenderer rend;
	private Editor editor;

	/**
	 * creates a new track-bar instance
	 * 
	 * @param editor
	 *            the editor owning the build tool
	 */
	public TrackBar(Editor editor) {
		this.editor = editor;
		this.rend = new TrackBarRenderer(this);
	}

	/**
	 * provides the index of the currently selected tile
	 * 
	 * @return the current tile index
	 */
	int getIndex() {
		return this.index;
	}

	/**
	 * provides the renderer responsible for rendering the track-bar
	 * 
	 * @return the renderer
	 */
	public IRenderer getRenderer() {
		return this.rend;
	}

	/**
	 * activates the build tool with the currently selected tile
	 */
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

	/**
	 * performs an update tick on the "tool"
	 */
	public void tick() {
		this.index = -1;
		if (this.checkForHotKeys(this.editor.getController().getKeyboard()))
			return;
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

	/**
	 * performs hotkey checks for quick track piece access
	 * 
	 * @param k
	 *            the keyboard to operate with
	 * @return {@code true} if a hotkey was used (terminates tick),
	 *         {@code false} otherwise
	 * @since 1.4 (SimTrack 0.2.2)
	 */
	private boolean checkForHotKeys(Keyboard k) {
		if (k.key(KeyEvent.VK_MINUS))
			this.index = 0;
		else if (k.key(KeyEvent.VK_1))
			this.index = 1;
		else if (k.key(KeyEvent.VK_2))
			this.index = 2;
		else if (k.key(KeyEvent.VK_3))
			this.index = 3;
		else if (k.key(KeyEvent.VK_4))
			this.index = 4;
		else if (k.key(KeyEvent.VK_5))
			this.index = 5;
		else if (k.key(KeyEvent.VK_6))
			this.index = 6;
		else if (k.key(KeyEvent.VK_7))
			this.index = 7;
		else if (k.key(KeyEvent.VK_8))
			this.index = 8;
		else if (k.key(KeyEvent.VK_9))
			this.index = 9;
		else if (k.key(KeyEvent.VK_0))
			this.index = 10;
		else
			return false;
		this.select();
		return true;

	}

}