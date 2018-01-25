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
package ch.awae.simtrack.view;

import java.util.ArrayList;

import ch.awae.simtrack.model.IModel;

/**
 * factory for view instance
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.1
 */
public class ViewFactory {

	/**
	 * creates a new game view instance
	 * 
	 * @param model
	 * @param hooker
	 * @return a new game view instance
	 */
	public static IView createGameView(IModel model, IGUIHookProvider hooker) {
		GameView v = new GameView(model, hooker.getScreenWidth(),
				hooker.getScreenHeight());
		v.setRenderingDelegate(hooker.getRenderDelegate());
		hooker.hookComponentRenderer(v::render);
		ArrayList<IRenderer> rends = new ArrayList<>();
		rends.add(new TileRenderer());
		rends.add(new HexGridRenderer());
		v.setRenderers(rends);
		return v;
	}
}
