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

import ch.awae.simtrack.Global;
import ch.awae.simtrack.gui.Window;
import ch.awae.simtrack.model.position.TileCoordinate;

/**
 * Mouse Observer
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-22
 * @since SimTrack 0.0.1
 */
public class Mouse {

	Point mouse;
	TileCoordinate mouseHex;
	boolean b1;
	boolean b2;
	boolean b3;
	double scroll;
	private MouseAdapter adapter;
	long lastScrollUpdate;
	private final static long MAX_SCROLL_TIME_WITHOUT_UPDATE = 100;

	// BASIC SETUP
	{
		this.mouse = new Point(0, 0);
		this.mouseHex = new TileCoordinate(0, 0);
		this.b1 = this.b2 = this.b3 = false;
		this.scroll = 0;
	}

	// ADAPTER IMPLEMENTATION
	{
		this.adapter = new MouseAdapter() {

			private void setBtn(int button, boolean value) {
				switch (button) {
				case MouseEvent.BUTTON1:
					Mouse.this.b1 = value;
					break;
				case MouseEvent.BUTTON2:
					Mouse.this.b2 = value;
					break;
				case MouseEvent.BUTTON3:
					Mouse.this.b3 = value;
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
				Mouse.this.mouse = p;
				Point scene = Global.port.getSceneCoordinate(p);
				TileCoordinate tile = Global.port.getHexPos(scene);
				// if (tile != null)
				Mouse.this.mouseHex = tile;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				this.mouseMoved(e);
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Mouse.this.scroll = e.getPreciseWheelRotation();
				Mouse.this.lastScrollUpdate = System.currentTimeMillis();
			}

		};
	}

	public Mouse(Window w) {
		w.getContentPane().addMouseListener(this.adapter);
		w.getContentPane().addMouseMotionListener(this.adapter);
		w.getContentPane().addMouseWheelListener(this.adapter);
	}

	public Point position() {
		return this.mouse;
	}

	public TileCoordinate hexPosition() {
		return this.mouseHex;
	}

	public double getScroll() {
		return (this.lastScrollUpdate + MAX_SCROLL_TIME_WITHOUT_UPDATE < System
				.currentTimeMillis()) ? 0 : this.scroll;
	}

	public boolean button1() {
		return this.b1;
	}

	public boolean button2() {
		return this.b2;
	}

	public boolean button3() {
		return this.b3;
	}

}
