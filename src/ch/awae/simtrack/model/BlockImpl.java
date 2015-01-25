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

import java.util.ArrayList;
import java.util.List;

import ch.awae.simtrack.model.ISignal.State;

// TODO: Auto-generated Javadoc
/**
 * The Class BlockImpl.
 *
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-24
 * @since SimTrack 0.2.2
 */
class BlockImpl implements IBlock {

	// TODO: >> optimise pre-signal state implementation.

	/** The members. */
	private ArrayList<ITile> members;
	
	/** The exits. */
	private ArrayList<ISignal> entrances, exits;
	
	/** The presig. */
	private boolean presig;

	/** The reserver. */
	private ITrain occupant, reserver;

	{
		this.members = new ArrayList<>();
		this.entrances = new ArrayList<>();
		this.exits = new ArrayList<>();
		this.occupant = this.reserver = null;
		this.presig = true;
	}

	/**
	 * Instantiates a new block impl.
	 *
	 * @param tiles the tiles
	 */
	BlockImpl(ITile... tiles) {
		for (ITile tile : tiles)
			this.members.add(tile);
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IEntity#tick()
	 */
	@Override
	public void tick() {
		for (ISignal exit : this.exits) {
			if (exit.isExitPresignal() && exit.getState() != State.OPEN) {
				this.presig = false;
				return;
			}
		}
		this.presig = true;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IEntity#update(ch.awae.simtrack.model.IModel)
	 */
	@Override
	public void update(IModel model) {
		// TODO: signal list update
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#merge(ch.awae.simtrack.model.IBlock)
	 */
	@Override
	public void merge(IBlock block) {
		for (ITile t : block.getTiles()) {
			t.setBlock(this);
			this.members.add(t);
		}
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#getTiles()
	 */
	@Override
	public List<ITile> getTiles() {
		return this.members;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#getEntrySignals()
	 */
	@Override
	public List<ISignal> getEntrySignals() {
		return this.entrances;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#getExitSignals()
	 */
	@Override
	public List<ISignal> getExitSignals() {
		return this.exits;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#hasOpenExitPresignal()
	 */
	@Override
	public boolean hasOpenExitPresignal() {
		return this.presig;
	}

	// == OCCUPANCY MANAGEMENT ==

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#isOccupied()
	 */
	@Override
	public boolean isOccupied() {
		return this.occupant != null;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#isReserved()
	 */
	@Override
	public boolean isReserved() {
		return this.reserver != null;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#occupy(ch.awae.simtrack.model.ITrain)
	 */
	@Override
	public void occupy(ITrain occupant) {
		this.occupant = occupant;
		this.reserver = null;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#getOccupant()
	 */
	@Override
	public ITrain getOccupant() {
		return this.occupant;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#getReserver()
	 */
	@Override
	public ITrain getReserver() {
		return this.reserver;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#reserve(ch.awae.simtrack.model.ITrain)
	 */
	@Override
	public void reserve(ITrain reserver) {
		this.reserver = reserver;
		this.occupant = null;
	}

	/* (non-Javadoc)
	 * @see ch.awae.simtrack.model.IBlock#free()
	 */
	@Override
	public void free() {
		this.reserver = this.occupant = null;
	}

}
