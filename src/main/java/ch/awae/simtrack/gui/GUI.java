package ch.awae.simtrack.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.function.Consumer;

import ch.awae.simtrack.controller.GUIControllerHook;
import ch.awae.simtrack.view.GUIRenderHook;
import ch.awae.simtrack.view.Graphics;

/**
 * The GUI wrapper class
 * 
 * @author Andreas Wälchli
 * @version 1.2, 2015-01-26
 * @since SimTrack 0.2.2
 */
public class GUI implements GUIControllerHook, GUIRenderHook {

	private Window window;
	private Surface surface;

	/**
	 * instantiates a new GUI with the provided dimensions
	 * 
	 * @param x
	 *            the width of the window in pixels
	 * @param y
	 *            the height of the window in pixels
	 */
	public GUI(int x, int y) {
		this.window = new Window(x, y);
		this.surface = this.window.getSurface();
	}

	@Override
	public Runnable getRenderDelegate() {
		return () -> this.surface.doPaint();
	}

	@Override
	public void hookComponentRenderer(Consumer<Graphics> consumer) {
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

	@Override
	public Consumer<String> getWindowTitleHookup() {
		return s -> window.setTitle("Simtrack : " + s);
	}

}
