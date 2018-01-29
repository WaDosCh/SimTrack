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

import ch.awae.simtrack.view.IRenderer;

/**
 * describes the basic behaviour of any editor tool. Each editor tool represents
 * a possible editor state.
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.0.1
 */
public interface ITool {

	/**
	 * retrieves the renderer for this tool
	 * 
	 * @return the tool renderer
	 */
	public IRenderer getRenderer();

	/**
	 * provides the tool identification string. This will be used to identify
	 * tools for loading them
	 * 
	 * @return the tool identifier
	 */
	public String getToolName();

	/**
	 * Loads the tool. This method should be used in case a tool requires setup
	 * of other external elements.
	 * 
	 * @param args
	 *            additional parameters
	 * @throws IllegalStateException
	 *             if the tool cannot be loaded at the moment. The editor will
	 *             fall back to the last tool used.
	 */
	public void load(Object[] args) throws IllegalStateException;

	/**
	 * performs an update tick on the tool
	 */
	public void tick();

	/**
	 * Unloads the tool. This method signals the tool that it will be
	 * deactivated. Any external cleanups should occur here. Other than the
	 * {@link #load(Object[])} method, this method cannot deny deactivation.
	 */
	default void onUnload() {};

}
