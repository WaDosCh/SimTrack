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
package ch.awae.simtrack.model;

/**
 * Qualifies a tile for rotations. Rotations and mirroring allow for adjustment
 * of a tile before and after placing it.
 * 
 * @author Andreas Wälchli
 * @version 1.1 (2015-01-16)
 * @since SimTrack 0.0.1 (2015-01-16)
 */
public interface RotatableTile {

	/**
	 * rotates the part by one edge (60 degrees). This method should only ever
	 * be called by the relevant controller. Calls from outside the defined
	 * pathways may lead to cache inconsistencies.
	 * 
	 * @param isClockwise
	 *            indicates whether the rotation should occur clockwise (
	 *            {@code true}) or counter-clockwise ({@code false})
	 */
	public void rotate(boolean isClockwise);

	/**
	 * mirrors the part. The mirroring axis is not standardised and may be
	 * freely chosen by the specific implementation. If no mirroring action is
	 * desired, this method can simply return directly. This method should only
	 * ever be called by the relevant controller. Calls from outside the defined
	 * pathways may lead to cache inconsistencies.
	 */
	public void mirror();

}
