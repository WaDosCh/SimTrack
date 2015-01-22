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
package ch.awae.simtrack.controller.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.model.position.TileCoordinate;
import ch.awae.simtrack.view.SceneViewPort;

/**
 * Mouse Observer
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-22
 * @since SimTrack 0.1.1 (0.0.1)
 */
public class Mouse {

	static Point mouse;
	static TileCoordinate mouseHex;
	static boolean b1, b2, b3;
	static double scroll;
	private static MouseAdapter adapter;
	static long lastScrollUpdate;
	private final static long MAX_SCROLL_TIME_WITHOUT_UPDATE = 100;

	// BASIC SETUP
	static {
		mouse = new Point(0, 0);
		mouseHex = new TileCoordinate(0, 0);
		b1 = b2 = b3 = false;
		scroll = 0;
	}

	// ADAPTER IMPLEMENTATION
	static {
		adapter = new MouseAdapter() {

			private void setBtn(int button, boolean value) {
				switch (button) {
				case MouseEvent.BUTTON1:
					Mouse.b1 = value;
					break;
				case MouseEvent.BUTTON2:
					Mouse.b2 = value;
					break;
				case MouseEvent.BUTTON3:
					Mouse.b3 = value;
					break;
				default:
					break;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				this.setBtn(e.getButton(), true);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				this.setBtn(e.getButton(), false);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				if (p == null)
					return;
				Mouse.mouse = p;
				Point scene = SceneViewPort.getSceneCoordinate(p);
				TileCoordinate tile = SceneViewPort.getHexPos(scene);
				// if (tile != null)
				Mouse.mouseHex = tile;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				this.mouseMoved(e);
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Mouse.scroll = e.getPreciseWheelRotation();
				Mouse.lastScrollUpdate = System.currentTimeMillis();
			}

		};
	}

	public static void init() {
		Window.INSTANCE.getContentPane().addMouseListener(adapter);
		Window.INSTANCE.getContentPane().addMouseMotionListener(adapter);
		Window.INSTANCE.getContentPane().addMouseWheelListener(adapter);
	}

	public static Point position() {
		return mouse.getLocation();
	}

	public static TileCoordinate hexPosition() {
		return mouseHex;
	}

	public static double getScroll() {
		return (lastScrollUpdate + MAX_SCROLL_TIME_WITHOUT_UPDATE < System
				.currentTimeMillis()) ? 0 : scroll;
	}

	public static boolean button1() {
		return b1;
	}

	public static boolean button2() {
		return b2;
	}

	public static boolean button3() {
		return b3;
	}

}
