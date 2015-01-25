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

import java.util.List;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-23
 * @since SimTrack 0.2.1
 */
public interface IBlock extends IEntity {

	public void merge(IBlock block);

	public List<ITile> getTiles();

	public List<ISignal> getEntrySignals();

	public List<ISignal> getExitSignals();

	public boolean hasOpenExitPresignal();

	public boolean isOccupied();

	public boolean isReserved();

	public void occupy(ITrain occupant);

	public ITrain getOccupant();

	public ITrain getReserver();

	public void reserve(ITrain reserver);

	public void free();

}