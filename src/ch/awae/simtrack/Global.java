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
package ch.awae.simtrack;

import ch.awae.simtrack.controller.Editor;
import ch.awae.simtrack.controller.RenderingController;
import ch.awae.simtrack.controller.input.Keyboard;
import ch.awae.simtrack.controller.input.Mouse;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.model.Map;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Globally relevant data. Most of this will probably be replaced by singletons
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class Global {

	public static Window window;
	public static RenderingController rc;
	public static Map map;
	public static SceneViewPort port;
	public static Mouse mouse;
	public static Editor editor;
	public static int ScreenW;
	public static int ScreenH;
	public static Keyboard keyboard;

}
