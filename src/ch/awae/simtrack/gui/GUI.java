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
package ch.awae.simtrack.gui;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.function.Consumer;

import ch.awae.simtrack.controller.IGUIControllerHookup;
import ch.awae.simtrack.view.IGUIHookProvider;

/**
 * @author Andreas Wälchli
 * @version 1.1, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class GUI implements IGUIControllerHookup, IGUIHookProvider {

	private Window window;
	private Surface surface;

	public GUI(int x, int y) {
		this.window = new Window(x, y);
		this.surface = this.window.getSurface();
	}

	@Override
	public Runnable getRenderDelegate() {
		return this.window::repaint;
	}

	@Override
	public void hookComponentRenderer(Consumer<Graphics2D> consumer) {
		this.surface.setRenderingHook(consumer);
	}

	@Override
	public int getScreenWidth() {
		return this.surface.getWidth();
	}

	@Override
	public int getScreenHeight() {
		return this.surface.getHeight();
	}

	@Override
	public Consumer<MouseAdapter> getMouseHookup() {
		return (adap) -> {
			this.surface.addMouseListener(adap);
			this.surface.addMouseMotionListener(adap);
			this.surface.addMouseWheelListener(adap);
		};
	}

	@Override
	public Consumer<KeyAdapter> getKeyboardHookup() {
		return this.window::addKeyListener;
	}

}
