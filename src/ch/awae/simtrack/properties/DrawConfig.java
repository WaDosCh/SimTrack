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
package ch.awae.simtrack.properties;

/**
 * This enum contains all possible draw configs. The actual colouring and
 * drawing according to a particular config must be handled by the drawing code.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 *
 */
public enum DrawConfig {

	/**
	 * Represents the config for placed tiles. All elements should be visible
	 */
	SOLID(false, false),
	/**
	 * Represents a ghost view of a tile. This is used as a hovering preview
	 * while editing. This config should be complied to by the use of minimal
	 * elements and a consistent grey colouring.
	 */
	GHOST(true, true),
	/**
	 * Represents an invalid ghost view. This is used to indicate an invalid
	 * position in the hovering preview. This config should be complied to by
	 * the use of the same elements as in the normal ghost config, and by the
	 * use of a red colouring instead of the grey one.
	 */
	INVALID_GHOST(true, false);

	DrawConfig(boolean isGhost, boolean isPlaceable) {
		this.isGhost = isGhost;
		this.isPlaceable = isPlaceable;
	}

	/**
	 * Indicates whether "ghost mode" is required or not.
	 */
	public final boolean isGhost;

	/**
	 * Indicates whether the ghost is valid or not. This must only be complied
	 * to when drawing a ghost.
	 */
	public final boolean isPlaceable;

}
