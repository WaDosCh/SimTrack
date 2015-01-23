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

import ch.awae.simtrack.model.position.DirectedTileEdgeCoordinate;

/**
 * Basic description of a signal.
 * 
 * @author Andreas Wälchli
 * @version 2.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface ISignal extends IEntity {

	public DirectedTileEdgeCoordinate getPosition();

	public void setPosition(DirectedTileEdgeCoordinate position);

	public IBlock getPreviousBlock();

	public IBlock getSubsequentBlock();

	public State getState();

	public boolean isEntryPresignal();

	public boolean setEntryPresignal(boolean value);

	public boolean isExitPresignal();

	public boolean setExitPresignal(boolean value);

	public static enum State {
		OPEN, RESERVED, CLOSED, ERROR;
	}

}
